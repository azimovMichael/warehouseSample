package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.WarehouseApp;

import com.mycompany.myapp.domain.Cell;
import com.mycompany.myapp.repository.CellRepository;
import com.mycompany.myapp.service.CellService;
import com.mycompany.myapp.service.dto.CellDTO;
import com.mycompany.myapp.service.mapper.CellMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Area;
/**
 * Test class for the CellResource REST controller.
 *
 * @see CellResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApp.class)
public class CellResourceIntTest {

    private static final Area DEFAULT_AREA = Area.GOOD;
    private static final Area UPDATED_AREA = Area.BAD;

    private static final Long DEFAULT_ROW = 1L;
    private static final Long UPDATED_ROW = 2L;

    private static final Long DEFAULT_CELL_COLUMN = 1L;
    private static final Long UPDATED_CELL_COLUMN = 2L;

    @Inject
    private CellRepository cellRepository;

    @Inject
    private CellMapper cellMapper;

    @Inject
    private CellService cellService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCellMockMvc;

    private Cell cell;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CellResource cellResource = new CellResource();
        ReflectionTestUtils.setField(cellResource, "cellService", cellService);
        this.restCellMockMvc = MockMvcBuilders.standaloneSetup(cellResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createEntity(EntityManager em) {
        Cell cell = new Cell()
                .area(DEFAULT_AREA)
                .row(DEFAULT_ROW)
                .cellColumn(DEFAULT_CELL_COLUMN);
        return cell;
    }

    @Before
    public void initTest() {
        cell = createEntity(em);
    }

    @Test
    @Transactional
    public void createCell() throws Exception {
        int databaseSizeBeforeCreate = cellRepository.findAll().size();

        // Create the Cell
        CellDTO cellDTO = cellMapper.cellToCellDTO(cell);

        restCellMockMvc.perform(post("/api/cells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cellDTO)))
            .andExpect(status().isCreated());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeCreate + 1);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testCell.getRow()).isEqualTo(DEFAULT_ROW);
        assertThat(testCell.getCellColumn()).isEqualTo(DEFAULT_CELL_COLUMN);
    }

    @Test
    @Transactional
    public void createCellWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cellRepository.findAll().size();

        // Create the Cell with an existing ID
        Cell existingCell = new Cell();
        existingCell.setId(1L);
        CellDTO existingCellDTO = cellMapper.cellToCellDTO(existingCell);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCellMockMvc.perform(post("/api/cells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCellDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCells() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList
        restCellMockMvc.perform(get("/api/cells?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].row").value(hasItem(DEFAULT_ROW.intValue())))
            .andExpect(jsonPath("$.[*].cellColumn").value(hasItem(DEFAULT_CELL_COLUMN.intValue())));
    }

    @Test
    @Transactional
    public void getCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get the cell
        restCellMockMvc.perform(get("/api/cells/{id}", cell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cell.getId().intValue()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.row").value(DEFAULT_ROW.intValue()))
            .andExpect(jsonPath("$.cellColumn").value(DEFAULT_CELL_COLUMN.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCell() throws Exception {
        // Get the cell
        restCellMockMvc.perform(get("/api/cells/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Update the cell
        Cell updatedCell = cellRepository.findOne(cell.getId());
        updatedCell
                .area(UPDATED_AREA)
                .row(UPDATED_ROW)
                .cellColumn(UPDATED_CELL_COLUMN);
        CellDTO cellDTO = cellMapper.cellToCellDTO(updatedCell);

        restCellMockMvc.perform(put("/api/cells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cellDTO)))
            .andExpect(status().isOk());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testCell.getRow()).isEqualTo(UPDATED_ROW);
        assertThat(testCell.getCellColumn()).isEqualTo(UPDATED_CELL_COLUMN);
    }

    @Test
    @Transactional
    public void updateNonExistingCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Create the Cell
        CellDTO cellDTO = cellMapper.cellToCellDTO(cell);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCellMockMvc.perform(put("/api/cells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cellDTO)))
            .andExpect(status().isCreated());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        int databaseSizeBeforeDelete = cellRepository.findAll().size();

        // Get the cell
        restCellMockMvc.perform(delete("/api/cells/{id}", cell.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
