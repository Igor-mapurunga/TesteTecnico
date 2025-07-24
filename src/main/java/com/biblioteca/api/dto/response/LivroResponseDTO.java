package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

import java.math.BigDecimal;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        BigDecimal preco,
        AutorResponseDTO autor,
        CategoriaResponseDTO categoria
) {

    public LivroResponseDTO(Livro livro) {
        this(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getPreco(),
                new AutorResponseDTO(
                        livro.getAutor().getId(),
                        livro.getAutor().getNome(),
                        livro.getAutor().getEmail(),
                        livro.getAutor().getDataNascimento()
                ),
                new CategoriaResponseDTO(
                        livro.getCategoria().getId(),
                        livro.getCategoria().getNome(),
                        livro.getCategoria().getDescricao()
                )
        );
    }
}
