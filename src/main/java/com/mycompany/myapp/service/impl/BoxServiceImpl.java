package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BoxService;
import com.mycompany.myapp.domain.Box;
import com.mycompany.myapp.repository.BoxRepository;
import com.mycompany.myapp.service.dto.BoxDTO;
import com.mycompany.myapp.service.mapper.BoxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Box.
 */
@Service
@Transactional
public class BoxServiceImpl implements BoxService{

    private final Logger log = LoggerFactory.getLogger(BoxServiceImpl.class);
    
    @Inject
    private BoxRepository boxRepository;

    @Inject
    private BoxMapper boxMapper;

    /**
     * Save a box.
     *
     * @param boxDTO the entity to save
     * @return the persisted entity
     */
    public BoxDTO save(BoxDTO boxDTO) {
        log.debug("Request to save Box : {}", boxDTO);
        Box box = boxMapper.boxDTOToBox(boxDTO);
        box = boxRepository.save(box);
        BoxDTO result = boxMapper.boxToBoxDTO(box);
        return result;
    }

    /**
     *  Get all the boxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Boxes");
        Page<Box> result = boxRepository.findAll(pageable);
        return result.map(box -> boxMapper.boxToBoxDTO(box));
    }

    /**
     *  Get one box by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BoxDTO findOne(Long id) {
        log.debug("Request to get Box : {}", id);
        Box box = boxRepository.findOne(id);
        BoxDTO boxDTO = boxMapper.boxToBoxDTO(box);
        return boxDTO;
    }

    /**
     *  Delete the  box by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Box : {}", id);
        boxRepository.delete(id);
    }
}
