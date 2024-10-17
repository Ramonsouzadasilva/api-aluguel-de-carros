package com.AluguelDeCarros.dto.reserva;

import com.AluguelDeCarros.entity.TipoSeguro;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Optional;


public record ReservaResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") Date dataDeEntrada,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") Date dataDeSaida,
        CarroReservadoResponse carro,
        double valorTotalDoAluguel,
        double valorAdicionalDoSeguro,
        Long totalDeDias,
        TipoSeguro tipoSeguro
) {}

