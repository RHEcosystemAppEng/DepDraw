package com.redhat.depdraw.dataservice.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.dataservice.dao.model.Diagram;

@ApplicationScoped
public class DiagramService {

    @Inject
    DiagramDao diagramDao;

    public Diagram createDiagram(Diagram diagram){
        final UUID uuid = UUID.randomUUID();
        diagram.setUuid(uuid.toString());

        return diagramDao.create(diagram);
    }

    public Diagram getDiagramById(String diagramId) {
        return diagramDao.getDiagramById(diagramId);
    }

    public void deleteDiagramById(String diagramId) {
        final Diagram diagram = getDiagramById(diagramId);

        if(diagram != null) {
            diagramDao.deleteDiagramById(diagramId);
        }
    }

    public List<Diagram> getDiagrams() {
        return diagramDao.getDiagrams();
    }

    public Diagram updateDiagram(Diagram diagram) {
        return diagramDao.updateDiagram(diagram);
    }
}
