package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.WarehouseApp;

import com.mycompany.myapp.domain.Compactus;
import com.mycompany.myapp.repository.CompactusRepository;
import com.mycompany.myapp.service.CompactusService;
import com.mycompany.myapp.service.dto.CompactusDTO;
import com.mycompany.myapp.service.mapper.CompactusMapper;

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

import com.mycompany.myapp.domain.enumeration.Warehouse;
/**
 * Test class for the CompactusResource REST controller.
 *
 * @see CompactusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApp.class)
public class CompactusResourceIntTest {

    private static final Warehouse DEFAULT_WAREHOUSE = Warehouse.ONE;
    private static final Warehouse UPDATED_WAREHOUSE = Warehouse.TWO;

    @Inject
    private CompactusRepository compactusRepository;

    @Inject
    private CompactusMapper compactusMapper;

    @Inject
    private CompactusService compactusService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCompactusMockMvc;

    private Compactus compactus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompactusResource compactusResource = new CompactusResource();
        ReflectionTestUtils.setField(compactusResource, "compactusService", compactusService);
        this.restCompactusMockMvc = MockMvcBuilders.standaloneSetup(compactusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compactus createEntity(EntityManager em) {
        Compactus compactus = new Compactus()
                .warehouse(DEFAULT_WAREHOUSE);
        return compactus;
    }

    @Before
    public void initTest() {
        compactus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompactus() throws Exception {
        int databaseSizeBeforeCreate = compactusRepository.findAll().size();

        // Create the Compactus
        CompactusDTO compactusDTO = compactusMapper.compactusToCompactusDTO(compactus);

        restCompactusMockMvc.perform(post("/api/compactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compactusDTO)))
            .andExpect(status().isCreated());

        // Validate the Compactus in the database
        List<Compactus> compactusList = compactusRepository.findAll();
        assertThat(compactusList).hasSize(databaseSizeBeforeCreate + 1);
        Compactus testCompactus = compactusList.get(compactusList.size() - 1);
        assertThat(testCompactus.getWarehouse()).isEqualTo(DEFAULT_WAREHOUSE);
    }

    @Test
    @Transactional
    public void createCompactusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compactusRepository.findAll().size();

        // Create the Compactus with an existing ID
        Compactus existingCompactus = new Compactus();
        existingCompactus.setId(1L);
        CompactusDTO existingCompactusDTO = compactusMapper.compactusToCompactusDTO(existingCompactus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompactusMockMvc.perform(post("/api/compactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCompactusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Compactus> compactusList = compactusRepository.findAll();
        assertThat(compactusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompactuses() throws Exception {
        // Initialize the database
        compactusRepository.saveAndFlush(compactus);

        // Get all the compactusList
        restCompactusMockMvc.perform(get("/api/compactuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compactus.getId().intValue())))
            .andExpect(jsonPath("$.[*].warehouse").value(hasItem(DEFAULT_WAREHOUSE.toString())));
    }

    @Test
    @Transactional
    public void getCompactus() throws Exception {
        // Initialize the database
        compactusRepository.saveAndFlush(compactus);

        // Get the compactus
        restCompactusMockMvc.perform(get("/api/compactuses/{id}", compactus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compactus.getId().intValue()))
            .andExpect(jsonPath("$.warehouse").value(DEFAULT_WAREHOUSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompactus() throws Exception {
        // Get the compactus
        restCompactusMockMvc.perform(get("/api/compactuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompactus() throws Exception {
        // Initialize the database
        compactusRepository.saveAndFlush(compactus);
        int databaseSizeBeforeUpdate = compactusRepository.findAll().size();

        // Update the compactus
        Compactus updatedCompactus = compactusRepository.findOne(compactus.getId());
        updatedCompactus
                .warehouse(UPDATED_WAREHOUSE);
        CompactusDTO compactusDTO = compactusMapper.compactusToCompactusDTO(updatedCompactus);

        restCompactusMockMvc.perform(put("/api/compactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compactusDTO)))
            .andExpect(status().isOk());

        // Validate the Compactus in the database
        List<Compactus> compactusList = compactusRepository.findAll();
        assertThat(compactusList).hasSize(databaseSizeBeforeUpdate);
        Compactus testCompactus = compactusList.get(compactusList.size() - 1);
        assertThat(testCompactus.getWarehouse()).isEqualTo(UPDATED_WAREHOUSE);
    }

    @Test
    @Transactional
    public void updateNonExistingCompactus() throws Exception {
        int databaseSizeBeforeUpdate = compactusRepository.findAll().size();

        // Create the Compactus
        CompactusDTO compactusDTO = compactusMapper.compactusToCompactusDTO(compactus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompactusMockMvc.perform(put("/api/compactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compactusDTO)))
            .andExpect(status().isCreated());

        // Validate the Compactus in the database
        List<Compactus> compactusList = compactusRepository.findAll();
        assertThat(compactusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompactus() throws Exception {
        // Initialize the database
        compactusRepository.saveAndFlush(compactus);
        int databaseSizeBeforeDelete = compactusRepository.findAll().size();

        // Get the compactus
        restCompactusMockMvc.perform(delete("/api/compactuses/{id}", compactus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Compactus> compactusList = compactusRepository.findAll();
        assertThat(compactusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
