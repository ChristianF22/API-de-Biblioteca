package br.com.ApiLibary.ApiLibary.controller;

import br.com.ApiLibary.ApiLibary.controller.dto.CadastroLivroDTO;
import br.com.ApiLibary.ApiLibary.controller.dto.ErroResposta;
import br.com.ApiLibary.ApiLibary.controller.dto.ResultadoPesquisaLivroDTO;
import br.com.ApiLibary.ApiLibary.controller.mappers.LivroMapper;
import br.com.ApiLibary.ApiLibary.entities.Livro;
import br.com.ApiLibary.ApiLibary.exceptions.RegistroDuplicadoException;
import br.com.ApiLibary.ApiLibary.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("livro")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvarLivro(@RequestBody @Valid CadastroLivroDTO dto){
            Livro livro = mapper.toEntity(dto);
            service.salvar(livro);
            URI localtion = gerarHeaderLocation(livro.getId());
            return ResponseEntity.created(localtion).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalheslivro(@PathVariable("id") String id){
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarLivro(@PathVariable("id") String id){
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    service.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
