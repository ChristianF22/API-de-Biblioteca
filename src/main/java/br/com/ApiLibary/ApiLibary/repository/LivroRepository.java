package br.com.ApiLibary.ApiLibary.repository;

import br.com.ApiLibary.ApiLibary.entities.Autor;
import br.com.ApiLibary.ApiLibary.entities.Livro;
import org.hibernate.query.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByAutor(Autor autor);

}