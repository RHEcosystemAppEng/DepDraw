package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.ResourceCatalog;

public interface DiagramResourceDao {
    DiagramResource create(String diagramId, String name, ResourceCatalog rc, String type, int posX, int posY);

    DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId);

    void deleteDiagramResourceById(String diagramId, String diagramResourceId);

    List<DiagramResource> getDiagramResources(String diagramId);

    void updateDefinition(String diagramId, String diagramResourceId, String definition);

    String getDefinition(String diagramId, String diagramResourceId);

}
