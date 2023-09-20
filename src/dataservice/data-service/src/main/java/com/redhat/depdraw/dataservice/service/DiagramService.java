package com.redhat.depdraw.dataservice.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.dto.DiagramDTO;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DiagramService {

    @Inject
    DiagramResourceService diagramResourceService;

    @Inject
    LineService lineService;

    @Inject
    DiagramDao diagramDao;

    @Transactional
    public Diagram createDiagram(String name){
        Diagram d = new Diagram();
        d.setName(name);

        return diagramDao.create(d);
    }

    public Diagram getDiagramById(String diagramId) {
        return diagramDao.getDiagramById(diagramId);
    }

    //TODo make sure it works properly
    @Transactional
    public void deleteDiagramById(String diagramId) {
        final Diagram diagram = getDiagramById(diagramId);

        if(diagram != null) {
            diagramDao.deleteDiagramById(diagramId);
        }
    }

    public List<Diagram> getDiagrams() {
        return diagramDao.getDiagrams();
    }

    @Transactional
    public Diagram updateDiagram(String uuid, DiagramDTO dto) {
        Map<String, DiagramResource> resourceSet = dto.getResourcesID().stream()
                .map(dr -> diagramResourceService.getDiagramResourceById(uuid, dr)).collect(Collectors.toMap(DiagramResource::getUuid, Function.identity()));
        Map<String, Line> lineSet = dto.getLinesID().stream()
                .map(l -> lineService.getLineById(uuid, l)).collect(Collectors.toMap(Line::getUuid, Function.identity()));

        Diagram d = new Diagram(uuid, dto.getName(), resourceSet, lineSet);

        return diagramDao.updateDiagram(d);
    }
}
