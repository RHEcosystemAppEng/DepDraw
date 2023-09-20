package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.model.Diagram;

public interface DiagramDao {
    Diagram create(Diagram diagram);

    Diagram getDiagramById(String diagramId);

    void deleteDiagramById(String diagramId);

    List<Diagram> getDiagrams();

    Diagram updateDiagram(Diagram diagram);
}
