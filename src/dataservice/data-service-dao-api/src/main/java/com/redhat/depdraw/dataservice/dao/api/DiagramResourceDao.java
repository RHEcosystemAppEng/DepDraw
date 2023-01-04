package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.model.DiagramResource;

public interface DiagramResourceDao {
    DiagramResource create(DiagramResource dr);

    DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId);

    void deleteDiagramResourceById(String diagramId, String diagramResourceId);

    List<DiagramResource> getDiagramResources(String diagramId);

    void updateDefinition(String diagramId, String diagramResourceId, String definition);

    String getDefinition(String diagramId, String diagramResourceId);

}
