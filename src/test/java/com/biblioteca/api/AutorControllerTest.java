package com.biblioteca.api;

import com.biblioteca.api.controller.AutorController;
import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.service.AutorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutorService autorService;

    @Test
    void deveCadastrarAutorComSucesso() throws Exception {
        AutorRequestDTO request = new AutorRequestDTO("João", "joao@email.com", LocalDate.of(1990, 1, 1));
        AutorResponseDTO response = new AutorResponseDTO(1L, "João", "joao@email.com", LocalDate.of(1990, 1, 1));

        when(autorService.criarAutor(any(AutorRequestDTO.class))).thenReturn(response);

        String requestBody = """
        {
            "nome": "João",
            "email": "joao@email.com",
            "dataNascimento": "1990-01-01"
        }
    """;

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.dataNascimento").value("1990-01-01"));
    }

    @Test
    void deveRetornarAutorPorId() throws Exception {
        AutorResponseDTO autor = new AutorResponseDTO(1L, "João", "joao@email.com", LocalDate.of(1990, 1, 1));
        when(autorService.buscarPorId(1L)).thenReturn(autor);

        mockMvc.perform(get("/api/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveRetornar404SeAutorNaoEncontrado() throws Exception {
        when(autorService.buscarPorId(99L)).thenThrow(new EntityNotFoundException("Autor não encontrado"));

        mockMvc.perform(get("/api/autores/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Autor não encontrado"));
    }
}
