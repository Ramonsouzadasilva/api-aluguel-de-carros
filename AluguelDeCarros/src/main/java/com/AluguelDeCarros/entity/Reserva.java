package com.AluguelDeCarros.entity;

import com.AluguelDeCarros.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dataDeEntrada;
    private Date dataDeSaida;

    @ManyToOne
    @JoinColumn(name = "carroId")
    private Carro carro;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

   private double valorTotalDoAluguel;
    private double ValorAdicionalDoSeguro;
    private Long totalDeDias;

    @Enumerated(EnumType.STRING)
    private TipoSeguro tipoSeguro;

}
