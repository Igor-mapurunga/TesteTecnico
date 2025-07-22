package com.biblioteca.api.dto.response;

import java.time.LocalDate;

public record AutorResponseDTO(
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {}
