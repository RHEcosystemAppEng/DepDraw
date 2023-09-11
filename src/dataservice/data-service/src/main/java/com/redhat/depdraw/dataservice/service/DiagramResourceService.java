package com.redhat.depdraw.dataservice.service;

import java.awt.*;
import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.dataservice.dao.api.DiagramResourceDao;
import com.redhat.depdraw.dto.DiagramResourceDTO;
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

    public DiagramResource createDiagramResource(String diagramId, DiagramResourceDTO drDTO) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(drDTO.getResourceCatalogID());
        DiagramResource dr = new DiagramResource("", drDTO.getName(), rc, drDTO.getType(), new Point(drDTO.getPosX(), drDTO.getPosY()), drDTO.getWidth(), drDTO.getHeight());
        final DiagramResource createdDiagramResource = diagramResourceDao.create(diagramId, dr);

        diagram.getResources().add(createdDiagramResource);

        diagramDao.updateDiagram(diagram);

        return createdDiagramResource;
    }

    public DiagramResource updateDiagramResource(String diagramId, String diagramResourceId, DiagramResourceDTO dto) {
        DiagramResource dr = getDiagramResourceById(diagramId, diagramResourceId);
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(dto.getResourceCatalogID());

        diagram.getResources().remove(dr);

        dr.setName(dto.getName());
        dr.setResourceCatalog(rc);
        dr.setType(dto.getType());
        dr.setPosition(new Point(dto.getPosX(), dto.getPosY()));
        dr.setWidth(dto.getWidth());
        dr.setHeight(dto.getHeight());
        final DiagramResource diagramResource = diagramResourceDao.updateDiagramResource(diagramId, dr);

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
