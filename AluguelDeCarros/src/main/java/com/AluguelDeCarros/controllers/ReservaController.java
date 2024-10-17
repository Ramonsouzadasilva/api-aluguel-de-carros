package com.AluguelDeCarros.controllers;

import com.AluguelDeCarros.dto.reserva.ReservaRequest;
import com.AluguelDeCarros.dto.reserva.ReservaResponse;
import com.AluguelDeCarros.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")

public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ReservaResponse reservarCarro(@RequestBody ReservaRequest request) {
        return reservaService.reservarCarro(request);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarReservas() {
        List<ReservaResponse> reservas = reservaService.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> atualizarDatasECarro(
            @PathVariable Long id,
            @RequestParam Date dataDeEntrada,
            @RequestParam Date dataDeSaida,
            @RequestParam String modeloCarro) {
        ReservaResponse response = reservaService.atualizarDatasECarro(id, dataDeEntrada, dataDeSaida, modeloCarro);
        return ResponseEntity.ok(response);
    }
}

