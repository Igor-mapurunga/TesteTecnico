package com.biblioteca.api;

import com.biblioteca.api.controller.CategoriaController;
import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.CategoriaService;
import com.biblioteca.api.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private LivroService livroService;

    @Test
    void deveRetornarListaVaziaDeCategorias() throws Exception {
        when(categoriaService.listarTodas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveCadastrarCategoriaComSucesso() throws Exception {
        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Programação", "Livros de tecnologia");

        when(categoriaService.criarCategoria(any(CategoriaRequestDTO.class))).thenReturn(response);

        String requestBody = """
            {
                "nome": "Programação",
                "descricao": "Livros de tecnologia"
            }
        """;

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Programação"))
                .andExpect(jsonPath("$.descricao").value("Livros de tecnologia"));
    }

    @Test
    void deveListarLivrosPorCategoria() throws Exception {
        LivroResponseDTO livro1 = new LivroResponseDTO(
                1L,
                "Clean Code",
                "9780132350884",
                2008,
                BigDecimal.valueOf(199.90),
                null,
                null
        );

        LivroResponseDTO livro2MesmoCategoria = new LivroResponseDTO(
                2L,
                "Refatoração",
                "9780134757599",
                2018,
                BigDecimal.valueOf(179.90),
                null,
                null
        );

        LivroResponseDTO livroOutraCategoria = new LivroResponseDTO(
                3L,
                "Batman: Ano Um",
                "9788573516399",
                2005,
                BigDecimal.valueOf(49.90),
                null,
                null
        );

        // Apenas os livros da categoria correta serão retornados pelo mock
        when(livroService.listarPorCategoria(1L)).thenReturn(List.of(livro1, livro2MesmoCategoria));

        mockMvc.perform(get("/api/categorias/1/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].titulo").value("Refatoração"));

        System.out.println(livroService.listarPorCategoria(1L));
    }
}
