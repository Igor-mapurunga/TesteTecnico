package com.biblioteca.api.dto.response;

import java.math.BigDecimal;

public record DadosLivroDTO(
        String titulo,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco
) {}
