package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CellDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Cell and its DTO CellDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CellMapper {

    @Mapping(source = "compactus.id", target = "compactusId")
    CellDTO cellToCellDTO(Cell cell);

    List<CellDTO> cellsToCellDTOs(List<Cell> cells);

    @Mapping(source = "compactusId", target = "compactus")
    Cell cellDTOToCell(CellDTO cellDTO);

    List<Cell> cellDTOsToCells(List<CellDTO> cellDTOs);

    default Compactus compactusFromId(Long id) {
        if (id == null) {
            return null;
        }
        Compactus compactus = new Compactus();
        compactus.setId(id);
        return compactus;
    }
}
