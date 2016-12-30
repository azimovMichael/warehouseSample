package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.WarehouseApp;

import com.mycompany.myapp.domain.Box;
import com.mycompany.myapp.repository.BoxRepository;
import com.mycompany.myapp.service.BoxService;
import com.mycompany.myapp.service.dto.BoxDTO;
import com.mycompany.myapp.service.mapper.BoxMapper;

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

import com.mycompany.myapp.domain.enumeration.BoxStatus;
/**
 * Test class for the BoxResource REST controller.
 *
 * @see BoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApp.class)
public class BoxResourceIntTest {

    private static final Long DEFAULT_MU_ID = 1L;
    private static final Long UPDATED_MU_ID = 2L;

    private static final BoxStatus DEFAULT_STATUS = BoxStatus.IN;
    private static final BoxStatus UPDATED_STATUS = BoxStatus.OUT;

    @Inject
    private BoxRepository boxRepository;

    @Inject
    private BoxMapper boxMapper;

    @Inject
    private BoxService boxService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBoxMockMvc;

    private Box box;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoxResource boxResource = new BoxResource();
        ReflectionTestUtils.setField(boxResource, "boxService", boxService);
        this.restBoxMockMvc = MockMvcBuilders.standaloneSetup(boxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Box createEntity(EntityManager em) {
        Box box = new Box()
                .muId(DEFAULT_MU_ID)
                .status(DEFAULT_STATUS);
        return box;
    }

    @Before
    public void initTest() {
        box = createEntity(em);
    }

    @Test
    @Transactional
    public void createBox() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box
        BoxDTO boxDTO = boxMapper.boxToBoxDTO(box);

        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate + 1);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getMuId()).isEqualTo(DEFAULT_MU_ID);
        assertThat(testBox.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box with an existing ID
        Box existingBox = new Box();
        existingBox.setId(1L);
        BoxDTO existingBoxDTO = boxMapper.boxToBoxDTO(existingBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoxes() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get all the boxList
        restBoxMockMvc.perform(get("/api/boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(box.getId().intValue())))
            .andExpect(jsonPath("$.[*].muId").value(hasItem(DEFAULT_MU_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", box.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(box.getId().intValue()))
            .andExpect(jsonPath("$.muId").value(DEFAULT_MU_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBox() throws Exception {
        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Update the box
        Box updatedBox = boxRepository.findOne(box.getId());
        updatedBox
                .muId(UPDATED_MU_ID)
                .status(UPDATED_STATUS);
        BoxDTO boxDTO = boxMapper.boxToBoxDTO(updatedBox);

        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isOk());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getMuId()).isEqualTo(UPDATED_MU_ID);
        assertThat(testBox.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingBox() throws Exception {
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Create the Box
        BoxDTO boxDTO = boxMapper.boxToBoxDTO(box);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxDTO)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);
        int databaseSizeBeforeDelete = boxRepository.findAll().size();

        // Get the box
        restBoxMockMvc.perform(delete("/api/boxes/{id}", box.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
