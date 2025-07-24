package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO autorCriado = autorService.criarAutor(dto);
        return ResponseEntity.status(201).body(autorCriado);
    }

    @GetMapping
    public ResponseEntity<Page<AutorResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(autorService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO atualizado = autorService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/livros")
    public List<LivroResponseDTO> listarLivrosDoAutor(@PathVariable Long id) {
        return autorService.listarPorAutor(id);
    }
}
