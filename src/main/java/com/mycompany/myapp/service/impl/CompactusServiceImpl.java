package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CompactusService;
import com.mycompany.myapp.domain.Compactus;
import com.mycompany.myapp.repository.CompactusRepository;
import com.mycompany.myapp.service.dto.CompactusDTO;
import com.mycompany.myapp.service.mapper.CompactusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Compactus.
 */
@Service
@Transactional
public class CompactusServiceImpl implements CompactusService{

    private final Logger log = LoggerFactory.getLogger(CompactusServiceImpl.class);
    
    @Inject
    private CompactusRepository compactusRepository;

    @Inject
    private CompactusMapper compactusMapper;

    /**
     * Save a compactus.
     *
     * @param compactusDTO the entity to save
     * @return the persisted entity
     */
    public CompactusDTO save(CompactusDTO compactusDTO) {
        log.debug("Request to save Compactus : {}", compactusDTO);
        Compactus compactus = compactusMapper.compactusDTOToCompactus(compactusDTO);
        compactus = compactusRepository.save(compactus);
        CompactusDTO result = compactusMapper.compactusToCompactusDTO(compactus);
        return result;
    }

    /**
     *  Get all the compactuses.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<CompactusDTO> findAll() {
        log.debug("Request to get all Compactuses");
        List<CompactusDTO> result = compactusRepository.findAll().stream()
            .map(compactusMapper::compactusToCompactusDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one compactus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CompactusDTO findOne(Long id) {
        log.debug("Request to get Compactus : {}", id);
        Compactus compactus = compactusRepository.findOne(id);
        CompactusDTO compactusDTO = compactusMapper.compactusToCompactusDTO(compactus);
        return compactusDTO;
    }

    /**
     *  Delete the  compactus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Compactus : {}", id);
        compactusRepository.delete(id);
    }
}
