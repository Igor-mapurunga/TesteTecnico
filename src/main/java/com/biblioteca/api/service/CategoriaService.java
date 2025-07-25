package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());

        categoria = categoriaRepository.save(categoria);
        return toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}

