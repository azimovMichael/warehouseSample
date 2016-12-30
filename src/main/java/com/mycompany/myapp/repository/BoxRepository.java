package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Box;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Box entity.
 */
@SuppressWarnings("unused")
public interface BoxRepository extends JpaRepository<Box,Long> {

}
