package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.BoxService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.BoxDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Box.
 */
@RestController
@RequestMapping("/api")
public class BoxResource {

    private final Logger log = LoggerFactory.getLogger(BoxResource.class);
        
    @Inject
    private BoxService boxService;

    /**
     * POST  /boxes : Create a new box.
     *
     * @param boxDTO the boxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boxDTO, or with status 400 (Bad Request) if the box has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boxes")
    @Timed
    public ResponseEntity<BoxDTO> createBox(@RequestBody BoxDTO boxDTO) throws URISyntaxException {
        log.debug("REST request to save Box : {}", boxDTO);
        if (boxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("box", "idexists", "A new box cannot already have an ID")).body(null);
        }
        BoxDTO result = boxService.save(boxDTO);
        return ResponseEntity.created(new URI("/api/boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("box", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boxes : Updates an existing box.
     *
     * @param boxDTO the boxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boxDTO,
     * or with status 400 (Bad Request) if the boxDTO is not valid,
     * or with status 500 (Internal Server Error) if the boxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boxes")
    @Timed
    public ResponseEntity<BoxDTO> updateBox(@RequestBody BoxDTO boxDTO) throws URISyntaxException {
        log.debug("REST request to update Box : {}", boxDTO);
        if (boxDTO.getId() == null) {
            return createBox(boxDTO);
        }
        BoxDTO result = boxService.save(boxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("box", boxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boxes : get all the boxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of boxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/boxes")
    @Timed
    public ResponseEntity<List<BoxDTO>> getAllBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Boxes");
        Page<BoxDTO> page = boxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /boxes/:id : get the "id" box.
     *
     * @param id the id of the boxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/boxes/{id}")
    @Timed
    public ResponseEntity<BoxDTO> getBox(@PathVariable Long id) {
        log.debug("REST request to get Box : {}", id);
        BoxDTO boxDTO = boxService.findOne(id);
        return Optional.ofNullable(boxDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /boxes/:id : delete the "id" box.
     *
     * @param id the id of the boxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBox(@PathVariable Long id) {
        log.debug("REST request to delete Box : {}", id);
        boxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("box", id.toString())).build();
    }

}
