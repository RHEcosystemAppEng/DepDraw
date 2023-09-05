package com.redhat.depdraw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "uuid")
@EqualsAndHashCode(exclude = "uuid")
public class LineDTO {
    private String uuid;

    private String lineCatalogID;

    private String source;

    private String destination;
}
