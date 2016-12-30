package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Compactus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Compactus entity.
 */
@SuppressWarnings("unused")
public interface CompactusRepository extends JpaRepository<Compactus,Long> {

}
