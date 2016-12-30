package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Warehouse;

/**
 * A DTO for the Compactus entity.
 */
public class CompactusDTO implements Serializable {

    private Long id;

    private Warehouse warehouse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompactusDTO compactusDTO = (CompactusDTO) o;

        if ( ! Objects.equals(id, compactusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompactusDTO{" +
            "id=" + id +
            ", warehouse='" + warehouse + "'" +
            '}';
    }
}
