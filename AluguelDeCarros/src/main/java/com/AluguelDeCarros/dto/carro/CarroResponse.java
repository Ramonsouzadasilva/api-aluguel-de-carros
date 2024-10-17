package com.AluguelDeCarros.dto.carro;

public record CarroResponse(Long id, String marca, Long ano, String modelo,
                            String descricao, double valorDaDiaria, boolean disponivel) {}

