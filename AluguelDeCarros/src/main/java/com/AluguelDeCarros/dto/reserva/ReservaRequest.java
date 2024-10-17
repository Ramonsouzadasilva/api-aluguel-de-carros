package com.AluguelDeCarros.dto.reserva;

import com.AluguelDeCarros.entity.TipoSeguro;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record ReservaRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") Date dataDeEntrada,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") Date dataDeSaida,
        Long carroId,
        TipoSeguro tipoSeguro
) {}

