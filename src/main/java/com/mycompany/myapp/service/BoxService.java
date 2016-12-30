package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.BoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Box.
 */
public interface BoxService {

    /**
     * Save a box.
     *
     * @param boxDTO the entity to save
     * @return the persisted entity
     */
    BoxDTO save(BoxDTO boxDTO);

    /**
     *  Get all the boxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" box.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BoxDTO findOne(Long id);

    /**
     *  Delete the "id" box.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
