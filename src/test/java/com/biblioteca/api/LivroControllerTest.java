package com.biblioteca.api;

import com.biblioteca.api.controller.LivroController;
import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarLivrosVazios() throws Exception {
        when(livroService.listarFiltrado(null, null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));


    }

    @Test
    void deveCriarLivro() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO(
                "Título de Teste",
                "1234567890",
                2020,
                new BigDecimal("49.90"),
                1L,
                1L
        );

        LivroResponseDTO response = new LivroResponseDTO(
                1L,
                request.titulo(),
                request.isbn(),
                request.anoPublicacao(),
                request.preco(),
                new AutorResponseDTO(1L, "Autor Teste", "autor@email.com", LocalDate.of(1990, 1, 1)),
                new CategoriaResponseDTO(1L, "Categoria Teste", "Descrição")
        );

        when(livroService.criarLivro(request)).thenReturn(response);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Título de Teste"))
                .andExpect(jsonPath("$.autor.nome").value("Autor Teste"));


    }

    @Test
    void deveRetornarNotFoundAoBuscarLivroInexistente() throws Exception {
        Long idInexistente = 99L;
        when(livroService.buscarPorId(idInexistente))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Livro não encontrado"));

        mockMvc.perform(get("/api/livros/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }
}
