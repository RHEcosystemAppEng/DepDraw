package com.redhat.depdraw.dataservice.service;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.model.Diagram;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiagramService {

    @Inject
    DiagramDao diagramDao;

    public Diagram createDiagram(Diagram diagram){
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
