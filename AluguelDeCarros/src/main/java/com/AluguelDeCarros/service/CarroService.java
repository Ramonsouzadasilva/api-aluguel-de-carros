package com.AluguelDeCarros.service;

import com.AluguelDeCarros.dto.carro.CarroRequest;
import com.AluguelDeCarros.dto.carro.CarroResponse;
import com.AluguelDeCarros.entity.Carro;
import com.AluguelDeCarros.exceptions.CarroNotFoundException;
import com.AluguelDeCarros.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public List<CarroResponse> listarCarrosDisponiveis() {
        return carroRepository.findAll().stream()
                .filter(Carro::isDisponivel)
                .map(carro -> new CarroResponse(carro.getId(), carro.getMarca(), carro.getAno(),
                        carro.getModelo(), carro.getDescricao(), carro.getValorDaDiaria(),
                        carro.isDisponivel())).collect(Collectors.toList());
    }

    public CarroResponse adicionarCarro(CarroRequest request) {
        Carro carro = new Carro(null, request.marca(), request.ano(), request.modelo(),
                request.descricao(), request.valorDaDiaria(), true);
        carro = carroRepository.save(carro);
        return new CarroResponse(carro.getId(), carro.getMarca(), carro.getAno(),
                carro.getModelo(), carro.getDescricao(), carro.getValorDaDiaria(), carro.isDisponivel());
    }

    public List<CarroResponse> listarCarrosComFiltrosLike(Integer ano, String modelo, String descricao) {
        return carroRepository.filtrarCarroslike(ano, modelo, descricao).stream()
                .map(carro -> new CarroResponse(carro.getId(), carro.getMarca(), carro.getAno(),
                        carro.getModelo(), carro.getDescricao(), carro.getValorDaDiaria(),
                        carro.isDisponivel()))
                .collect(Collectors.toList());
    }

    public CarroResponse atualizarCarro(Long id, CarroRequest request) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() ->  new CarroNotFoundException(id));

        carro.setMarca(request.marca());
        carro.setAno(request.ano());
        carro.setModelo(request.modelo());
        carro.setDescricao(request.descricao());
        carro.setValorDaDiaria(request.valorDaDiaria());
        carro.setDisponivel(true);
        carro = carroRepository.save(carro);
        return new CarroResponse(carro.getId(), carro.getMarca(), carro.getAno(),
                carro.getModelo(), carro.getDescricao(), carro.getValorDaDiaria(), carro.isDisponivel());
    }

    public void deletarCarro(Long id) {
        carroRepository.deleteById(id);
    }
}
