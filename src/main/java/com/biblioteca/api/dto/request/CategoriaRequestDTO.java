package com.biblioteca.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao
) {}