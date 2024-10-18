package com.AluguelDeCarros.repository;

import com.AluguelDeCarros.entity.Carro;
import com.AluguelDeCarros.entity.Reserva;
import com.AluguelDeCarros.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCarroAndDataDeSaidaAfterAndDataDeEntradaBefore
            (Carro carro, Date dataDeSaida, Date dataDeEntrada);
    List<Reserva> findByUser(User user);
}
