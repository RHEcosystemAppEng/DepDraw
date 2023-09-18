package com.redhat.depdraw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
@Entity
@Table(name = "resource_catalogs")
@NamedQueries({
        @NamedQuery(name="ResourceCatalog.findAll", query="SELECT rc FROM ResourceCatalog rc")
})
public class ResourceCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String uuid;

    private String name;

    @OneToMany(mappedBy = "resourceCatalog")
    @JsonIgnore
    private Set<DiagramResource> diagramResources;

    @OneToOne(mappedBy = "resourceCatalog", optional=false)
    private K8SResourceSchema k8sResourceSchema;
}
