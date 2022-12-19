package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.model.Diagram;

public interface DiagramDao {
    Diagram create(Diagram d);

    Diagram getDiagramById(String diagramId);

    void deleteDiagramById(String diagramId);

    List<Diagram> getDiagrams();

    Diagram updateDiagram(Diagram diagram);
}
