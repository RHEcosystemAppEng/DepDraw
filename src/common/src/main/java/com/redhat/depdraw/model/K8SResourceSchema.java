package com.redhat.depdraw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
@Entity
@Table(name = "k8s_resource_schema")
public class K8SResourceSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String uuid;

    @OneToOne
    @JoinColumn(name = "resource_catalog_id", referencedColumnName = "id")
    @JsonIgnore
    private ResourceCatalog resourceCatalog;

    @JdbcTypeCode(SqlTypes.JSON)
    private String data;
}
