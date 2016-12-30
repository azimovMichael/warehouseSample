package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CompactusDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Compactus and its DTO CompactusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompactusMapper {

    CompactusDTO compactusToCompactusDTO(Compactus compactus);

    List<CompactusDTO> compactusesToCompactusDTOs(List<Compactus> compactuses);

    Compactus compactusDTOToCompactus(CompactusDTO compactusDTO);

    List<Compactus> compactusDTOsToCompactuses(List<CompactusDTO> compactusDTOs);
}
