package br.com.ApiLibary.ApiLibary.service;

import br.com.ApiLibary.ApiLibary.entities.GeneroLivro;
import br.com.ApiLibary.ApiLibary.entities.Livro;
import br.com.ApiLibary.ApiLibary.repository.LivroRepository;
import br.com.ApiLibary.ApiLibary.validator.LivroValidador;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static br.com.ApiLibary.ApiLibary.repository.specification.LivroSpacification.*;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private  final LivroValidador validator;

    public Livro salvar(Livro livro) {
        validator.validar(livro);
        return repository.save(livro);
    }
    
    public Optional<Livro> obterPorId(UUID id){
        return repository.findById(id);
    }

    public void deletar(Livro livro){
        repository.delete(livro);
    }

    //Utilizando a Specifications para realizar o metodo de pesquisa
    public Page<Livro> pesquisa(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            Integer anoPublicacao,
            Integer pagina,
            Integer tamanhoPagina){

        //select * from livro where 1 = 1
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if(isbn != null){
            specs = specs.and(isbnEqual(isbn)); //query = query and isbn = :isbn
        }

        if(titulo != null){
            specs = specs.and(tituloLike(titulo));
        }

        if(genero != null){
            specs = specs.and(generoEqual(genero));
        }

        if(anoPublicacao != null){
            specs = specs.and(anoPublicacaoEquall(anoPublicacao));
        }

        if(nomeAutor != null){
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return repository.findAll(specs, pageRequest);
    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null){
            throw  new IllegalArgumentException("Para atualizar, é necessário que o livro já esteja salvo na base.");
        }
        validator.validar(livro);
        repository.save(livro);
    }
}
