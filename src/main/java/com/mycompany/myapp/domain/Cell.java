package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Area;

/**
 * A Cell.
 */
@Entity
@Table(name = "cell")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private Area area;

    @Column(name = "row")
    private Long row;

    @Column(name = "cell_column")
    private Long cellColumn;

    @ManyToOne
    private Compactus compactus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public Cell area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Long getRow() {
        return row;
    }

    public Cell row(Long row) {
        this.row = row;
        return this;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public Long getCellColumn() {
        return cellColumn;
    }

    public Cell cellColumn(Long cellColumn) {
        this.cellColumn = cellColumn;
        return this;
    }

    public void setCellColumn(Long cellColumn) {
        this.cellColumn = cellColumn;
    }

    public Compactus getCompactus() {
        return compactus;
    }

    public Cell compactus(Compactus compactus) {
        this.compactus = compactus;
        return this;
    }

    public void setCompactus(Compactus compactus) {
        this.compactus = compactus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell cell = (Cell) o;
        if (cell.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cell.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cell{" +
            "id=" + id +
            ", area='" + area + "'" +
            ", row='" + row + "'" +
            ", cellColumn='" + cellColumn + "'" +
            '}';
    }
}
