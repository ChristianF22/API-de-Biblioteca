package br.com.ApiLibary.ApiLibary.validator;

import br.com.ApiLibary.ApiLibary.entities.Livro;
import br.com.ApiLibary.ApiLibary.exceptions.CampoInvalidoExeception;
import br.com.ApiLibary.ApiLibary.exceptions.RegistroDuplicadoException;
import br.com.ApiLibary.ApiLibary.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidador {

    private static final int ANO_EXIGENCIA_PARA_PRECO_LIVRO = 2020;

    private final LivroRepository repository;

    public void validar(Livro livro){
        if(existeLivroComIsbn(livro)){
            throw new RegistroDuplicadoException("ISBN já cadastrado no sistema");
        }

        if(isPrecoObrigatorioNulo(livro)){
            throw new CampoInvalidoExeception("preco", "Para livros com o ano de publicação a partir de 2020, o preco é obrigatório.");
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null &&
                livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PARA_PRECO_LIVRO;
    }

    public boolean existeLivroComIsbn(Livro livro){
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());

        if(livro.getId() == null){
            return livroEncontrado.isPresent();
        }

        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }
}
