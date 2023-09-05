package com.redhat.depdraw.dataservice.service;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.DiagramResourceDao;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.ResourceCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiagramResourceService {

    @Inject
    DiagramResourceDao diagramResourceDao;

    @Inject
    DiagramService diagramService;

    @Inject
    ResourceCatalogService resourceCatalogService;

    public DiagramResource createDiagramResource(String diagramId, String name, String resourceCatalogID, String type, int posX, int posY) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(resourceCatalogID);
        final DiagramResource createdDiagramResource = diagramResourceDao.create(diagramId, name, rc, type, posX, posY);

        diagram.getResources().add(createdDiagramResource);

        diagramService.updateDiagram(diagram);

        return createdDiagramResource;
    }

    public DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId) {
        return diagramResourceDao.getDiagramResourceById(diagramId, diagramResourceId);
    }

    public void deleteDiagramResourceById(String diagramId, String diagramResourceId) {
        DiagramResource diagramResource = getDiagramResourceById(diagramId, diagramResourceId);
        diagramResourceDao.deleteDiagramResourceById(diagramId, diagramResourceId);

        final Diagram diagram = diagramService.getDiagramById(diagramId);
        diagram.getResources().remove(diagramResource);

        diagramService.updateDiagram(diagram);
    }

    public List<DiagramResource> getDiagramResources(String diagramId) {
        return diagramResourceDao.getDiagramResources(diagramId);
    }

    public void updateDiagramResourceDefinition(String diagramId, String resourceId, String definition) {
        diagramResourceDao.updateDefinition(diagramId, resourceId, definition);
    }

    public String getDiagramResourceDefinition(String diagramId, String resourceId) {
        return diagramResourceDao.getDefinition(diagramId, resourceId);
    }
}
