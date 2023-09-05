package com.redhat.depdraw.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
public class DiagramResourceDTO {
    private String uuid;

    private String name;

    private String resourceCatalogID;

    private String type;

    private int posX;

    private int posY;
}