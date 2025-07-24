package com.biblioteca.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImportarLivroRequestDTO(

        @NotBlank(message = "A URL não pode estar em branco")
        String url,

        @NotNull(message = "O ID do autor é obrigatório")
        Long autorId,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId

) {}
