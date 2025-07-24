package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.CategoriaService;
import com.biblioteca.api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO criada = categoriaService.criarCategoria(dto);
        return ResponseEntity.status(201).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}/livros")
    public ResponseEntity<List<LivroResponseDTO>> listarLivrosPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.listarPorCategoria(id));
    }


}
