package com.redhat.depdraw.model;

import java.util.Set;

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
@Table(name = "line_catalogs")
@NamedQueries({
        @NamedQuery(name="LineCatalog.findAll", query="SELECT lc FROM LineCatalog lc")
})
public class LineCatalog {
    public static final String INHERIT_LABELS = "Inherit Labels";
    public static final String INHERIT_ANNOTATIONS = "Inherit Annotations";
    public static final String INHERIT_METADATA = "Inherit Metadata";
    public static final String SELECT_RESOURCE = "Select Resource";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "line_catalog_rules", joinColumns = @JoinColumn(name = "line_catalog_id"))
    @Column(name = "rule")
    private Set<String> rules;
}
