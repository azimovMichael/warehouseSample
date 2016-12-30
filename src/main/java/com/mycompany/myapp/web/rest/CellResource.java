package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.CellService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.service.dto.CellDTO;

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
 * REST controller for managing Cell.
 */
@RestController
@RequestMapping("/api")
public class CellResource {

    private final Logger log = LoggerFactory.getLogger(CellResource.class);
        
    @Inject
    private CellService cellService;

    /**
     * POST  /cells : Create a new cell.
     *
     * @param cellDTO the cellDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellDTO, or with status 400 (Bad Request) if the cell has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cells")
    @Timed
    public ResponseEntity<CellDTO> createCell(@RequestBody CellDTO cellDTO) throws URISyntaxException {
        log.debug("REST request to save Cell : {}", cellDTO);
        if (cellDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cell", "idexists", "A new cell cannot already have an ID")).body(null);
        }
        CellDTO result = cellService.save(cellDTO);
        return ResponseEntity.created(new URI("/api/cells/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cell", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cells : Updates an existing cell.
     *
     * @param cellDTO the cellDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellDTO,
     * or with status 400 (Bad Request) if the cellDTO is not valid,
     * or with status 500 (Internal Server Error) if the cellDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cells")
    @Timed
    public ResponseEntity<CellDTO> updateCell(@RequestBody CellDTO cellDTO) throws URISyntaxException {
        log.debug("REST request to update Cell : {}", cellDTO);
        if (cellDTO.getId() == null) {
            return createCell(cellDTO);
        }
        CellDTO result = cellService.save(cellDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cell", cellDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cells : get all the cells.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cells in body
     */
    @GetMapping("/cells")
    @Timed
    public List<CellDTO> getAllCells() {
        log.debug("REST request to get all Cells");
        return cellService.findAll();
    }

    /**
     * GET  /cells/:id : get the "id" cell.
     *
     * @param id the id of the cellDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cellDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cells/{id}")
    @Timed
    public ResponseEntity<CellDTO> getCell(@PathVariable Long id) {
        log.debug("REST request to get Cell : {}", id);
        CellDTO cellDTO = cellService.findOne(id);
        return Optional.ofNullable(cellDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cells/:id : delete the "id" cell.
     *
     * @param id the id of the cellDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cells/{id}")
    @Timed
    public ResponseEntity<Void> deleteCell(@PathVariable Long id) {
        log.debug("REST request to delete Cell : {}", id);
        cellService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cell", id.toString())).build();
    }

}
