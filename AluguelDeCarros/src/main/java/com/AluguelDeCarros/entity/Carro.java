package com.AluguelDeCarros.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Carro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "A marca não pode estar em branco.")
    private String marca;

    @NotNull(message = "O ano é obrigatório.")
    private Long ano;

    @NotBlank(message = "O modelo não pode estar em branco.")
    private String modelo;

    @NotBlank(message = "A descrição não pode estar em branco.")
    private String descricao;

    @NotNull(message = "O valor da diária é obrigatório.")
    private double valorDaDiaria;

    private boolean disponivel;
}
