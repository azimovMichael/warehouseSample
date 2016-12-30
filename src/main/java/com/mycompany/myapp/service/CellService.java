package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CellDTO;
import java.util.List;

/**
 * Service Interface for managing Cell.
 */
public interface CellService {

    /**
     * Save a cell.
     *
     * @param cellDTO the entity to save
     * @return the persisted entity
     */
    CellDTO save(CellDTO cellDTO);

    /**
     *  Get all the cells.
     *  
     *  @return the list of entities
     */
    List<CellDTO> findAll();

    /**
     *  Get the "id" cell.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CellDTO findOne(Long id);

    /**
     *  Delete the "id" cell.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
