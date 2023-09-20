package com.redhat.depdraw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
@Entity
@Table(name = "lines")
@NamedQueries({
        @NamedQuery(name="Line.findByDiagramId", query="SELECT d.lines FROM Diagram d where d.id = :id"),
        @NamedQuery(name="Line.deleteLineByDiagramResourceId", query="DELETE FROM Line l where l.destination.id = :diagramResourceId or l.source.id = :diagramResourceId")
})
public class Line {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name="line_id") // join column is in table for Line
    @JsonIgnore
    private Diagram diagram;

    @ManyToOne(optional=false)
    @JoinColumn(name = "line_catalog_id", nullable = false)
    private LineCatalog lineCatalog;

    @ManyToOne(optional=false)
    @JoinColumn(name="source_diagram_resource_id", nullable = false)
    private DiagramResource source;

    @ManyToOne(optional=false)
    @JoinColumn(name="destination_diagram_resource_id", nullable = false)
    private DiagramResource destination;
}
