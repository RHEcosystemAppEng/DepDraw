package com.redhat.depdraw.model;

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
public class Line {
    private String uuid;

    private LineCatalog lineCatalog;

    private DiagramResource source;

    private DiagramResource destination;
}
