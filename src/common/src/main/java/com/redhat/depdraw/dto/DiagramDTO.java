package com.redhat.depdraw.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
public class DiagramDTO {
    private String uuid;

    private String name;

    private Set<String> resourcesID = Set.of();

    private Set<String> linesID = Set.of();
}
