package com.biblioteca.api.dto.response;

import java.math.BigDecimal;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco,
        AutorResponseDTO autor,
        CategoriaResponseDTO categoria
) {}