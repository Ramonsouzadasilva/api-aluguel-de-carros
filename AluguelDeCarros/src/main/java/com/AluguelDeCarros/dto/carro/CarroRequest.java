package com.AluguelDeCarros.dto.carro;

public record CarroRequest(String marca, Long ano, String modelo, String descricao,
                           double valorDaDiaria) {}

