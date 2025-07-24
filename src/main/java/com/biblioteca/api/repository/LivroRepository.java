package com.biblioteca.api.repository;

import com.biblioteca.api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutorId(Long autorId);

}
