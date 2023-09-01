package com.redhat.depdraw.fe.model;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
public class DiagramFE {
    private String uuid;

    private String name;

    private Set<String> resourcesID = Set.of();

    private Set<String> linesID = Set.of();
}
