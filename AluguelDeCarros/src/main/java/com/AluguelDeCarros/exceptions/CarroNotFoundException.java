package com.AluguelDeCarros.exceptions;

public class CarroNotFoundException extends RuntimeException {
    public CarroNotFoundException(Long id) {
        super("Não foi possível encontrar um carro com o ID: " + id + ". Verifique se o ID está correto e tente novamente.");
    }

    public CarroNotFoundException(String message) {
        super(message);
    }
}
