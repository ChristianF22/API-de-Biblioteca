package br.com.ApiLibary.ApiLibary.controller;

import br.com.ApiLibary.ApiLibary.controller.dto.AutorDTO;
import br.com.ApiLibary.ApiLibary.controller.dto.ErroResposta;
import br.com.ApiLibary.ApiLibary.controller.mappers.AutorMapper;
import br.com.ApiLibary.ApiLibary.entities.Autor;
import br.com.ApiLibary.ApiLibary.exceptions.OperacaoNaoPerminitidaException;
import br.com.ApiLibary.ApiLibary.exceptions.RegistroDuplicadoException;
import br.com.ApiLibary.ApiLibary.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class AutorController implements GenericController{

    private final AutorService service;
    private final AutorMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvarAutor(@RequestBody @Valid AutorDTO dto){
            Autor autor = mapper.toEntity(dto);
            service.salvar(autor);
            URI localtion = gerarHeaderLocation(autor.getId());
            return ResponseEntity.created(localtion).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhesAutor(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorid(idAutor);
        return service
                .obterPorid(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarAutor(@PathVariable("id") String id){
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorid(idAutor);
            if(autorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            service.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisarAutor(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
        List<Autor> resultado = service.pesquisa(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizarAutor(@PathVariable("id") String id, @RequestBody @Valid AutorDTO dto){
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorid(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var autor = autorOptional.get();
            autor.setNome(dto.nome());
            autor.setNacionalidade(dto.nacionalidade());
            autor.setDataNascimento(dto.dataNascimento());
            service.atualizar(autor);
            return ResponseEntity.noContent().build();

    }
}
