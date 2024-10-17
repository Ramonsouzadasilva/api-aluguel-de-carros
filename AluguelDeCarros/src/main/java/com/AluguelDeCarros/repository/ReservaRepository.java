package com.AluguelDeCarros.repository;

import com.AluguelDeCarros.entity.Carro;
import com.AluguelDeCarros.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCarroAndDataDeSaidaAfterAndDataDeEntradaBefore
            (Carro carro, Date dataDeSaida, Date dataDeEntrada);
}
