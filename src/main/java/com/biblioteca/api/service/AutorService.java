package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public AutorResponseDTO criarAutor(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setEmail(dto.email());
        autor.setDataNascimento(dto.dataNascimento());

        autor = autorRepository.save(autor);
        return toResponseDTO(autor);
    }

    public Page<AutorResponseDTO> listarTodos(Pageable pageable) {
        return autorRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public AutorResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarAutorExistente(id));
    }

    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = buscarAutorExistente(id);
        autor.setNome(dto.nome());
        autor.setEmail(dto.email());
        autor.setDataNascimento(dto.dataNascimento());
        return toResponseDTO(autorRepository.save(autor));
    }

    private Autor buscarAutorExistente(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
    }

    public List<LivroResponseDTO> listarPorAutor(Long autorId) {
        if (!autorRepository.existsById(autorId)) {
            throw new EntityNotFoundException("Autor não encontrado");
        }
        List<Livro> livros = livroRepository.findByAutorId(autorId);
        return livros.stream()
                .map(LivroResponseDTO::new)
                .toList();
    }

    @Transactional
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
