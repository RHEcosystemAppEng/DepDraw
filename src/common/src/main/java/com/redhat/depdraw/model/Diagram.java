package com.redhat.depdraw.model;

import java.util.HashMap;
import java.util.Map;

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
@Table(name = "diagrams")
@Entity
@NamedQueries({
        @NamedQuery(name="Diagram.findAll", query="SELECT d FROM Diagram d")
})
public class Diagram {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String name;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy="diagram")
    @MapKey
    private Map<String, DiagramResource> resources = new HashMap<>();

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy="diagram")
    @MapKey
    private Map<String, Line> lines = new HashMap<>();
}
