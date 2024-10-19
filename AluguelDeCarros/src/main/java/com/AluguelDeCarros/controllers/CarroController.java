package com.AluguelDeCarros.controllers;

import com.AluguelDeCarros.dto.carro.CarroRequest;
import com.AluguelDeCarros.dto.carro.CarroResponse;
import com.AluguelDeCarros.service.CarroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping("/filtros")
    public ResponseEntity<List<CarroResponse>> listarCarrosComFiltrosLikeAtualizado(
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "descricao", required = false) String descricao) {
        return ResponseEntity.ok(carroService.listarCarrosComFiltrosLike(ano, modelo, descricao));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<CarroResponse>> listarCarrosDisponiveis() {
        List<CarroResponse> carros = carroService.listarCarrosDisponiveis();
        return ResponseEntity.ok(carros);
    }

    @PostMapping
    public ResponseEntity<CarroResponse> adicionarCarro(@RequestBody CarroRequest request) {
        CarroResponse carroCriado = carroService.adicionarCarro(request);
        return new ResponseEntity<>(carroCriado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroResponse> atualizarCarro(@PathVariable Long id, @RequestBody CarroRequest request) {
        return ResponseEntity.ok(carroService.atualizarCarro(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCarro(@PathVariable Long id) {
        carroService.deletarCarro(id);
        return ResponseEntity.noContent().build();
    }
}

