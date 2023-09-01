package com.redhat.depdraw.fe.model;

import lombok.*;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
public class DiagramResourceFE {
    private String uuid;

    private String name;

    private String resourceCatalogID;

    private String diagramID;

    private String type;

    private Point position;
}
