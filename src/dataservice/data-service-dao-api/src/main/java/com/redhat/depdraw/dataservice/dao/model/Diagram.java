package com.redhat.depdraw.dataservice.dao.model;

import java.util.List;
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

    private Set<String> resourcesID = Set.of();

    private Set<String> linesID = Set.of();
}
