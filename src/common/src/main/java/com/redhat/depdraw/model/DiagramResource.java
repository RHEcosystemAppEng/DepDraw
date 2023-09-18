package com.redhat.depdraw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
@Entity
@Table(name = "diagram_resources")
@NamedQueries({
        @NamedQuery(name="DiagramResource.findByDiagramId", query="SELECT d.resources FROM Diagram d where d.id = :id")
})
public class DiagramResource {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String name;

    @ManyToOne
    @JoinColumn(name="diagram_id") // join column is in table for Diagram
    @JsonIgnore
    private Diagram diagram;

    @OneToOne(optional=false)
    @JoinColumn(name = "resource_catalog_id", referencedColumnName = "id")
    private ResourceCatalog resourceCatalog;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "line_id", referencedColumnName = "id")
    @JsonIgnore
    private Line line;

    private String type;

    private Point position;

    private int width;

    private int height;

    @JdbcTypeCode(SqlTypes.JSON)
    private String definition;
}
