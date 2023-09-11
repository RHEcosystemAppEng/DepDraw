package com.redhat.depdraw.dataservice.service;

import java.awt.*;
import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
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
    DiagramDao diagramDao;

    @Inject
    ResourceCatalogService resourceCatalogService;

    public DiagramResource createDiagramResource(String diagramId, String name, String resourceCatalogID, String type, int posX, int posY) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(resourceCatalogID);
        DiagramResource dr = new DiagramResource("", name, rc, type, new Point(posX, posY));
        final DiagramResource createdDiagramResource = diagramResourceDao.create(diagramId, dr);

        diagram.getResources().add(createdDiagramResource);

        diagramDao.updateDiagram(diagram);

        return createdDiagramResource;
    }

    public DiagramResource updateDiagramResource(String diagramId, String uuid, String name, String resourceCatalogID, String type, int posX, int posY) {
        DiagramResource originalDr = getDiagramResourceById(diagramId, uuid);
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(resourceCatalogID);
        DiagramResource dr = diagramResourceDao.getDiagramResourceById(diagramId, uuid);
        dr.setName(name);
        dr.setResourceCatalog(rc);
        dr.setType(type);
        dr.setPosition(new Point(posX, posY));

        final DiagramResource diagramResource = diagramResourceDao.updateDiagramResource(diagramId, dr);

        diagram.getResources().remove(originalDr);
        diagram.getResources().add(diagramResource);

        diagramDao.updateDiagram(diagram);

        return diagramResource;
    }

    public DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId) {
        return diagramResourceDao.getDiagramResourceById(diagramId, diagramResourceId);
    }

    public void deleteDiagramResourceById(String diagramId, String diagramResourceId) {
        DiagramResource diagramResource = getDiagramResourceById(diagramId, diagramResourceId);
        diagramResourceDao.deleteDiagramResourceById(diagramId, diagramResourceId);

        final Diagram diagram = diagramService.getDiagramById(diagramId);
        diagram.getResources().remove(diagramResource);

        diagramDao.updateDiagram(diagram);
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
