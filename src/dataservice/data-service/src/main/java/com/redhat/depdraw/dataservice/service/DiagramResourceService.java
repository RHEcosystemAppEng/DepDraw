package com.redhat.depdraw.dataservice.service;

import com.redhat.depdraw.dataservice.dao.api.DiagramResourceDao;
import com.redhat.depdraw.dto.DiagramResourceDTO;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.ResourceCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DiagramResourceService {

    @Inject
    DiagramResourceDao diagramResourceDao;

    @Inject
    DiagramService diagramService;

    @Inject
    ResourceCatalogService resourceCatalogService;

    @Transactional
    public DiagramResource createDiagramResource(String diagramId, DiagramResourceDTO drDTO) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(drDTO.getResourceCatalogID());

        DiagramResource dr = new DiagramResource();
        dr.setName(drDTO.getName());
        dr.setResourceCatalog(rc);
        dr.setType(drDTO.getType());
        dr.setPosition(new Point(drDTO.getPosX(), drDTO.getPosY()));
        dr.setWidth(drDTO.getWidth());
        dr.setHeight(drDTO.getHeight());
        dr.setDiagram(diagram);

        return diagramResourceDao.create(diagramId, dr);
    }

    @Transactional
    public DiagramResource updateDiagramResource(String diagramId, String diagramResourceId, DiagramResourceDTO dto) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        final ResourceCatalog rc = resourceCatalogService.getResourceCatalogById(dto.getResourceCatalogID());

        DiagramResource dr = diagram.getResources().get(diagramResourceId);

        dr.setName(dto.getName());
        dr.setResourceCatalog(rc);
        dr.setType(dto.getType());
        dr.setPosition(new Point(dto.getPosX(), dto.getPosY()));
        dr.setWidth(dto.getWidth());
        dr.setHeight(dto.getHeight());
        dr.setDiagram(diagram);

        return diagramResourceDao.updateDiagramResource(diagramId, dr);
    }

    public DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId) {
        return diagramResourceDao.getDiagramResourceById(diagramId, diagramResourceId);
    }

    @Transactional
    public void deleteDiagramResourceById(String diagramId, String diagramResourceId) {
        diagramResourceDao.deleteDiagramResourceById(diagramId, diagramResourceId);
    }

    public List<DiagramResource> getDiagramResources(String diagramId) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);

        return new ArrayList<>(diagram.getResources().values());
    }

    @Transactional
    public void updateDiagramResourceDefinition(String diagramId, String resourceId, String definition) {
        diagramResourceDao.updateDefinition(diagramId, resourceId, definition);
    }

    public String getDiagramResourceDefinition(String diagramId, String resourceId) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);

        DiagramResource dr = diagram.getResources().get(resourceId);

        return dr.getDefinition();
    }
}
