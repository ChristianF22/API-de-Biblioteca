package br.com.ApiLibary.ApiLibary.repository.specification;

import br.com.ApiLibary.ApiLibary.entities.GeneroLivro;
import br.com.ApiLibary.ApiLibary.entities.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpacification {

    public static Specification<Livro> isbnEqual(String isbn){
        // select * from livro where isbn = :isbn
        return (root, query, cb) -> cb.equal(root.get("isbn"),isbn);
    }

    public  static Specification<Livro> tituloLike(String titulo){
        // select * from livro where upper(titulo) like (%:param%)
        return (root, query, cb) -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, cb) -> cb.equal(root.get("genero"),genero);
    }

    public static Specification<Livro> anoPublicacaoEquall(Integer anoPublicacao){
        // and to_cha(data_publicacao, 'YYYY') = :anoPublicacao
        return (root, query, cb) -> cb.equal(cb.function("to_char", String.class,
                root.get("dataPublicacao"),cb.literal("YYYY")),anoPublicacao.toString());
    }

    public  static Specification<Livro> nomeAutorLike(String nome){
        return (root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER);
            return cb.like(cb.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%");

            //return cb.like(cb.upper(root.get("autor").get("nome")), "%" + nome.toUpperCase() + "%"); -- forma mais rapida e simples de fazer a consulta

        };
    }
}
