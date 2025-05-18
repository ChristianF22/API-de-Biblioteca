package br.com.ApiLibary.ApiLibary.service;


import br.com.ApiLibary.ApiLibary.entities.Autor;
import br.com.ApiLibary.ApiLibary.exceptions.OperacaoNaoPerminitidaException;
import br.com.ApiLibary.ApiLibary.repository.AutorRepository;
import br.com.ApiLibary.ApiLibary.repository.LivroRepository;
import br.com.ApiLibary.ApiLibary.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

    public Autor salvar(Autor autor){
        validator.validar(autor);
        return  repository.save(autor);
    }

    public void atualizar(Autor autor){
        if(autor.getId() == null){
            throw  new IllegalArgumentException("Para atualizar, é necessário que o autor já esteja salvo na base.");
        }
        validator.validar(autor);
        repository.save(autor);
    }

    public Optional<Autor> obterPorid(UUID id){
        return repository.findById(id);
    }

    public void deletar(Autor autor){
        if(possuiLivro(autor)){
            throw new OperacaoNaoPerminitidaException("Não é permitido excluir um autor que possui livros cadastrados!");
        }
        repository.delete(autor);
    }

    //Utilizando a Example para realizar o metodo de pesquisa
    public List<Autor> pesquisa(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, matcher);
        return repository.findAll(autorExample);
    }

    public boolean possuiLivro(Autor autor){
        return  livroRepository.existsByAutor(autor);
    }
}
