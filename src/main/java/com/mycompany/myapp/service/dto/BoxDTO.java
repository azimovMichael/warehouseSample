package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.BoxStatus;

/**
 * A DTO for the Box entity.
 */
public class BoxDTO implements Serializable {

    private Long id;

    private Long muId;

    private BoxStatus status;


    private Long cellId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMuId() {
        return muId;
    }

    public void setMuId(Long muId) {
        this.muId = muId;
    }
    public BoxStatus getStatus() {
        return status;
    }

    public void setStatus(BoxStatus status) {
        this.status = status;
    }

    public Long getCellId() {
        return cellId;
    }

    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoxDTO boxDTO = (BoxDTO) o;

        if ( ! Objects.equals(id, boxDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BoxDTO{" +
            "id=" + id +
            ", muId='" + muId + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
