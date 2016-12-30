package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.CompactusService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.service.dto.CompactusDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Compactus.
 */
@RestController
@RequestMapping("/api")
public class CompactusResource {

    private final Logger log = LoggerFactory.getLogger(CompactusResource.class);
        
    @Inject
    private CompactusService compactusService;

    /**
     * POST  /compactuses : Create a new compactus.
     *
     * @param compactusDTO the compactusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compactusDTO, or with status 400 (Bad Request) if the compactus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compactuses")
    @Timed
    public ResponseEntity<CompactusDTO> createCompactus(@RequestBody CompactusDTO compactusDTO) throws URISyntaxException {
        log.debug("REST request to save Compactus : {}", compactusDTO);
        if (compactusDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("compactus", "idexists", "A new compactus cannot already have an ID")).body(null);
        }
        CompactusDTO result = compactusService.save(compactusDTO);
        return ResponseEntity.created(new URI("/api/compactuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("compactus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /compactuses : Updates an existing compactus.
     *
     * @param compactusDTO the compactusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compactusDTO,
     * or with status 400 (Bad Request) if the compactusDTO is not valid,
     * or with status 500 (Internal Server Error) if the compactusDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/compactuses")
    @Timed
    public ResponseEntity<CompactusDTO> updateCompactus(@RequestBody CompactusDTO compactusDTO) throws URISyntaxException {
        log.debug("REST request to update Compactus : {}", compactusDTO);
        if (compactusDTO.getId() == null) {
            return createCompactus(compactusDTO);
        }
        CompactusDTO result = compactusService.save(compactusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("compactus", compactusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compactuses : get all the compactuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compactuses in body
     */
    @GetMapping("/compactuses")
    @Timed
    public List<CompactusDTO> getAllCompactuses() {
        log.debug("REST request to get all Compactuses");
        return compactusService.findAll();
    }

    /**
     * GET  /compactuses/:id : get the "id" compactus.
     *
     * @param id the id of the compactusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compactusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/compactuses/{id}")
    @Timed
    public ResponseEntity<CompactusDTO> getCompactus(@PathVariable Long id) {
        log.debug("REST request to get Compactus : {}", id);
        CompactusDTO compactusDTO = compactusService.findOne(id);
        return Optional.ofNullable(compactusDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /compactuses/:id : delete the "id" compactus.
     *
     * @param id the id of the compactusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compactuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompactus(@PathVariable Long id) {
        log.debug("REST request to delete Compactus : {}", id);
        compactusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("compactus", id.toString())).build();
    }

}
