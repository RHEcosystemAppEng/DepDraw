package com.redhat.depdraw.model;

import java.util.Set;

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
public class LineCatalog {
    public static final String INHERIT_LABELS = "Inherit Labels";
    public static final String INHERIT_ANNOTATIONS = "Inherit Annotations";
    public static final String INHERIT_METADATA = "Inherit Metadata";
    public static final String SELECT_RESOURCE = "Select Resource";

    private String uuid;

    private String name;

    private Set<String> rules;
}
