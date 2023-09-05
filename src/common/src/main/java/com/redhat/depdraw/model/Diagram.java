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
public class Diagram {
    private String uuid;

    private String name;

    private Set<DiagramResource> resourcesID = Set.of();

    private Set<Line> linesID = Set.of();
}
