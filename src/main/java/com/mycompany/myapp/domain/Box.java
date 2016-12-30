package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.BoxStatus;

/**
 * A Box.
 */
@Entity
@Table(name = "box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mu_id")
    private Long muId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BoxStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private Cell cell;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMuId() {
        return muId;
    }

    public Box muId(Long muId) {
        this.muId = muId;
        return this;
    }

    public void setMuId(Long muId) {
        this.muId = muId;
    }

    public BoxStatus getStatus() {
        return status;
    }

    public Box status(BoxStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BoxStatus status) {
        this.status = status;
    }

    public Cell getCell() {
        return cell;
    }

    public Box cell(Cell cell) {
        this.cell = cell;
        return this;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Box box = (Box) o;
        if (box.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, box.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Box{" +
            "id=" + id +
            ", muId='" + muId + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
