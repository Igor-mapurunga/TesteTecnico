package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO criado = livroService.criarLivro(dto);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar(
            @RequestParam(required = false) Long categoria,
            @RequestParam(required = false) Long autor,
            @RequestParam(required = false) Integer ano) {
        return ResponseEntity.ok(livroService.listarFiltrado(categoria, autor, ano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO atualizado = livroService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(livroService.buscarPorTitulo(titulo));
    }
}
