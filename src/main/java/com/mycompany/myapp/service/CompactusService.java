package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CompactusDTO;
import java.util.List;

/**
 * Service Interface for managing Compactus.
 */
public interface CompactusService {

    /**
     * Save a compactus.
     *
     * @param compactusDTO the entity to save
     * @return the persisted entity
     */
    CompactusDTO save(CompactusDTO compactusDTO);

    /**
     *  Get all the compactuses.
     *  
     *  @return the list of entities
     */
    List<CompactusDTO> findAll();

    /**
     *  Get the "id" compactus.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CompactusDTO findOne(Long id);

    /**
     *  Delete the "id" compactus.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
