package com.biblioteca.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN deve conter 10 ou 13 dígitos")
    private String isbn;

    @NotNull
    @Positive
    private Integer anoPublicacao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser positivo")
    private BigDecimal preco;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
