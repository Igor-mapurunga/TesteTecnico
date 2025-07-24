package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.CategoriaRepository;
import com.biblioteca.api.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public LivroResponseDTO criarLivro(LivroRequestDTO dto) {
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setPreco(dto.preco());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        livro = livroRepository.save(livro);
        return toResponseDTO(livro);
    }


    public List<LivroResponseDTO> listarPorCategoria(Long categoriaId) {
        return livroRepository.findAll().stream()
                .filter(livro -> livro.getCategoria().getId().equals(categoriaId))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));
        return toResponseDTO(livro);
    }

    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setPreco(dto.preco());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        livro = livroRepository.save(livro);
        return toResponseDTO(livro);
    }

    public void deletar(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new EntityNotFoundException("Livro não encontrado");
        }
        livroRepository.deleteById(id);
    }

    public List<LivroResponseDTO> listarFiltrado(Long categoriaId, Long autorId, Integer anoPublicacao) {
        return livroRepository.findAll().stream()
                .filter(livro -> (categoriaId == null || livro.getCategoria().getId().equals(categoriaId)) &&
                        (autorId == null || livro.getAutor().getId().equals(autorId)) &&
                        (anoPublicacao == null || livro.getAnoPublicacao().equals(anoPublicacao)))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LivroResponseDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findAll().stream()
                .filter(livro -> livro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private LivroResponseDTO toResponseDTO(Livro livro) {
        Autor autor = livro.getAutor();
        Categoria categoria = livro.getCategoria();

        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getPreco(),
                new AutorResponseDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getEmail(),
                        autor.getDataNascimento()
                ),
                new CategoriaResponseDTO(
                        categoria.getId(),
                        categoria.getNome(),
                        categoria.getDescricao()
                )
        );
    }
}
