package com.redhat.depdraw.dataservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiagramService {

    @Inject
    DiagramResourceService diagramResourceService;

    @Inject
    LineService lineService;

    @Inject
    DiagramDao diagramDao;

    public Diagram createDiagram(String name){
        return diagramDao.create(name);
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

    public Diagram updateDiagram(String uuid, String name, Set<String> resourcesID, Set<String> linesID) {
        Set<DiagramResource> resourceSet = resourcesID.stream()
                .map(dr -> diagramResourceService.getDiagramResourceById(uuid, dr)).collect(Collectors.toSet());
        Set<Line> lineSet = linesID.stream()
                .map(l -> lineService.getLineById(uuid, l)).collect(Collectors.toSet());

        Diagram d = new Diagram(uuid, name, resourceSet, lineSet);

        return diagramDao.updateDiagram(d);
    }
}
