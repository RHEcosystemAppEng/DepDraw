package com.redhat.depdraw.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DiagramDTO {
    private String name;

    private Set<String> resourcesID = Set.of();

    private Set<String> linesID = Set.of();
}
