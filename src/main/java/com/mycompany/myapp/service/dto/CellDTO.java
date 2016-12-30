package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Area;

/**
 * A DTO for the Cell entity.
 */
public class CellDTO implements Serializable {

    private Long id;

    private Area area;

    private Long row;

    private Long cellColumn;


    private Long compactusId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }
    public Long getCellColumn() {
        return cellColumn;
    }

    public void setCellColumn(Long cellColumn) {
        this.cellColumn = cellColumn;
    }

    public Long getCompactusId() {
        return compactusId;
    }

    public void setCompactusId(Long compactusId) {
        this.compactusId = compactusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CellDTO cellDTO = (CellDTO) o;

        if ( ! Objects.equals(id, cellDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CellDTO{" +
            "id=" + id +
            ", area='" + area + "'" +
            ", row='" + row + "'" +
            ", cellColumn='" + cellColumn + "'" +
            '}';
    }
}
