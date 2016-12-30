package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Box and its DTO BoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoxMapper {

    @Mapping(source = "cell.id", target = "cellId")
    BoxDTO boxToBoxDTO(Box box);

    List<BoxDTO> boxesToBoxDTOs(List<Box> boxes);

    @Mapping(source = "cellId", target = "cell")
    Box boxDTOToBox(BoxDTO boxDTO);

    List<Box> boxDTOsToBoxes(List<BoxDTO> boxDTOs);

    default Cell cellFromId(Long id) {
        if (id == null) {
            return null;
        }
        Cell cell = new Cell();
        cell.setId(id);
        return cell;
    }
}
