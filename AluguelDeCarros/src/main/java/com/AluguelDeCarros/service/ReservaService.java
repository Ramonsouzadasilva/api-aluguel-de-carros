package com.AluguelDeCarros.service;

import com.AluguelDeCarros.dto.reserva.CarroReservadoResponse;
import com.AluguelDeCarros.dto.reserva.ReservaRequest;
import com.AluguelDeCarros.dto.reserva.ReservaResponse;
import com.AluguelDeCarros.entity.Carro;
import com.AluguelDeCarros.entity.Reserva;
import com.AluguelDeCarros.entity.TipoSeguro;
import com.AluguelDeCarros.entity.user.User;
import com.AluguelDeCarros.exceptions.ReservaException;
import com.AluguelDeCarros.repository.CarroRepository;
import com.AluguelDeCarros.repository.ReservaRepository;
import com.AluguelDeCarros.repository.authorization.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private CarroRepository carroRepository;
    @Autowired
    private UserRepository userRepository;

    private long calcularTotalDeDias(Date dataDeEntrada, Date dataDeSaida) {
        if (dataDeSaida.before(dataDeEntrada)) {
            throw new IllegalArgumentException("A data de saída não pode ser anterior à data de entrada.");
        }
        return (dataDeSaida.getTime() - dataDeEntrada.getTime()) / (1000 * 60 * 60 * 24);
    }

    private double calcularValorTotalAluguel(double valorDiaria, long totalDeDias) {
        return valorDiaria * totalDeDias;
    }

    private double calcularAdicionalDoSeguro(double valorTotalDoAluguel, TipoSeguro tipoSeguro) {
        double adicional = 0.0;
        switch (tipoSeguro) {
            case BASICO:
                adicional = valorTotalDoAluguel * 0.10;
                break;
            case PREMIUM:
                adicional = valorTotalDoAluguel * 0.20;
                break;
            default:
                throw new IllegalArgumentException("Tipo de seguro inválido");
        }
        return adicional;
    }

    public ReservaResponse reservarCarro(ReservaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByLogin(username);

        Carro carro = carroRepository.findById(request.carroId())
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));

        List<Reserva> reservas = reservaRepository.findByCarroAndDataDeSaidaAfterAndDataDeEntradaBefore(
                carro, request.dataDeEntrada(), request.dataDeSaida());

        if (!reservas.isEmpty()) {
            throw new ReservaException("Carro já reservado nesse período");
        }

        double valorDiaria = carro.getValorDaDiaria();
        long totalDeDias = calcularTotalDeDias(request.dataDeEntrada(), request.dataDeSaida());
        double valorTotalDoAluguel = calcularValorTotalAluguel(valorDiaria, totalDeDias);
        double valorAdicionalDoSeguro = calcularAdicionalDoSeguro(valorTotalDoAluguel, request.tipoSeguro());

        Reserva reserva = new Reserva();
        reserva.setDataDeEntrada(request.dataDeEntrada());
        reserva.setDataDeSaida(request.dataDeSaida());
        reserva.setCarro(carro);
        reserva.setValorTotalDoAluguel(valorTotalDoAluguel + valorAdicionalDoSeguro);
        reserva.setValorAdicionalDoSeguro(valorAdicionalDoSeguro);
        reserva.setTotalDeDias(totalDeDias);
        reserva.setTipoSeguro(request.tipoSeguro());
        reserva.setUser(user);

        carro.setDisponivel(false);
        reserva = reservaRepository.save(reserva);

        return new ReservaResponse(
                reserva.getDataDeEntrada(),
                reserva.getDataDeSaida(),
                new CarroReservadoResponse(carro.getModelo(), carro.getAno(), carro.getValorDaDiaria()),
                reserva.getValorTotalDoAluguel(),
                reserva.getValorAdicionalDoSeguro(),
                reserva.getTotalDeDias(),
                reserva.getTipoSeguro()
        );
    }

    public List<ReservaResponse> listarReservas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByLogin(username);
//        List<Reserva> reservas = reservaRepository.findAll();
        List<Reserva> reservas = reservaRepository.findByUser(user);

        return reservas.stream()
                .map(reserva -> {
                    Carro carro = reserva.getCarro();
                    return new ReservaResponse(
                            reserva.getDataDeEntrada(),
                            reserva.getDataDeSaida(),
                            new CarroReservadoResponse(carro.getModelo(), carro.getAno(), carro.getValorDaDiaria()),
                            reserva.getValorTotalDoAluguel(),
                            reserva.getValorAdicionalDoSeguro(),
                            reserva.getTotalDeDias(),
                            reserva.getTipoSeguro()
                    );
                })
                .toList();
    }

    public void cancelarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    public ReservaResponse atualizarDatasECarro(Long reservaId, Date dataDeEntrada,
                                                Date dataDeSaida, String modeloCarro) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Carro carro = carroRepository.findByModeloAndDisponivelTrue(modeloCarro)
                .orElseThrow(() -> new RuntimeException("Carro não disponível"));

        List<Reserva> reservasExistentes = reservaRepository.findByCarroAndDataDeSaidaAfterAndDataDeEntradaBefore(
                carro, dataDeEntrada, dataDeSaida);
        if (!reservasExistentes.isEmpty() && !reservasExistentes.get(0).getId().equals(reservaId)) {
            throw new RuntimeException("Carro já reservado nesse período");
        }

        reserva.setDataDeEntrada(dataDeEntrada);
        reserva.setDataDeSaida(dataDeSaida);
        reserva.setCarro(carro);

        long totalDeDias = (dataDeSaida.getTime() - dataDeEntrada.getTime()) / (1000 * 60 * 60 * 24);
        double valorTotalDoAluguel = carro.getValorDaDiaria() * totalDeDias;

        reserva.setTotalDeDias(totalDeDias);
        reserva.setValorTotalDoAluguel(valorTotalDoAluguel);

        Reserva reservaAtualizada = reservaRepository.save(reserva);

        return new ReservaResponse(
                reserva.getDataDeEntrada(),
                reserva.getDataDeSaida(),
                new CarroReservadoResponse(carro.getModelo(), carro.getAno(), carro.getValorDaDiaria()),
                reserva.getValorTotalDoAluguel(),
                reserva.getValorAdicionalDoSeguro(),
                reserva.getTotalDeDias(),
                reserva.getTipoSeguro()
        );
    }

}
