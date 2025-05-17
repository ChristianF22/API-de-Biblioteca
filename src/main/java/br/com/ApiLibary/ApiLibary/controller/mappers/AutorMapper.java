package br.com.ApiLibary.ApiLibary.controller.mappers;

import br.com.ApiLibary.ApiLibary.controller.dto.AutorDTO;
import br.com.ApiLibary.ApiLibary.entities.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}