package com.redhat.depdraw.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DiagramResourceDTO {
    private String name;

    private String resourceCatalogID;

    private String type;

    private int posX;

    private int posY;

    private int width;

    private int height;
}