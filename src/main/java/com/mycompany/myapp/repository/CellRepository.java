package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cell;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cell entity.
 */
@SuppressWarnings("unused")
public interface CellRepository extends JpaRepository<Cell,Long> {

}
