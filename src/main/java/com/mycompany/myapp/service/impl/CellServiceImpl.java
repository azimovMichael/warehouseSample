package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CellService;
import com.mycompany.myapp.domain.Cell;
import com.mycompany.myapp.repository.CellRepository;
import com.mycompany.myapp.service.dto.CellDTO;
import com.mycompany.myapp.service.mapper.CellMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Cell.
 */
@Service
@Transactional
public class CellServiceImpl implements CellService{

    private final Logger log = LoggerFactory.getLogger(CellServiceImpl.class);
    
    @Inject
    private CellRepository cellRepository;

    @Inject
    private CellMapper cellMapper;

    /**
     * Save a cell.
     *
     * @param cellDTO the entity to save
     * @return the persisted entity
     */
    public CellDTO save(CellDTO cellDTO) {
        log.debug("Request to save Cell : {}", cellDTO);
        Cell cell = cellMapper.cellDTOToCell(cellDTO);
        cell = cellRepository.save(cell);
        CellDTO result = cellMapper.cellToCellDTO(cell);
        return result;
    }

    /**
     *  Get all the cells.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<CellDTO> findAll() {
        log.debug("Request to get all Cells");
        List<CellDTO> result = cellRepository.findAll().stream()
            .map(cellMapper::cellToCellDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one cell by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CellDTO findOne(Long id) {
        log.debug("Request to get Cell : {}", id);
        Cell cell = cellRepository.findOne(id);
        CellDTO cellDTO = cellMapper.cellToCellDTO(cell);
        return cellDTO;
    }

    /**
     *  Delete the  cell by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cell : {}", id);
        cellRepository.delete(id);
    }
}
