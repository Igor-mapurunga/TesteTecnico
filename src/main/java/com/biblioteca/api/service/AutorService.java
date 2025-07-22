package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public AutorResponseDTO criarAutor(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setEmail(dto.email());
        autor.setDataNascimento(dto.dataNascimento());

        autor = autorRepository.save(autor);
        return toResponseDTO(autor);
    }

    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AutorResponseDTO buscarPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
        return toResponseDTO(autor);
    }

    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));

        autor.setNome(dto.nome());
        autor.setEmail(dto.email());
        autor.setDataNascimento(dto.dataNascimento());

        autor = autorRepository.save(autor);
        return toResponseDTO(autor);
    }

    public void deletar(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new EntityNotFoundException("Autor não encontrado");
        }
        autorRepository.deleteById(id);
    }

    private AutorResponseDTO toResponseDTO(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getEmail(),
                autor.getDataNascimento()
        );
    }
}
