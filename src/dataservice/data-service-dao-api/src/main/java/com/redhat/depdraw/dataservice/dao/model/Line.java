package com.redhat.depdraw.dataservice.dao.model;

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

    private String diagramID;

    private String lineCatalogID;

    private String source;

    private String destination;
}
