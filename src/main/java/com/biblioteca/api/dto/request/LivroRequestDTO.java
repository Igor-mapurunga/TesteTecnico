package com.biblioteca.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LivroRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "ISBN é obrigatório")
        @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN deve conter 10 ou 13 dígitos")
        String isbn,

        @NotNull(message = "Ano de publicação é obrigatório")
        @Positive(message = "Ano de publicação deve ser positivo")
        Integer anoPublicacao,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "ID do autor é obrigatório")
        Long autorId,

        @NotNull(message = "ID da categoria é obrigatório")
        Long categoriaId
) {}