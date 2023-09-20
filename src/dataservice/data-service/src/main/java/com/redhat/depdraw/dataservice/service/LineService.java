package com.redhat.depdraw.dataservice.service;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.LineDao;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LineService {
    @Inject
    DiagramService diagramService;

    @Inject
    LineCatalogService lineCatalogService;

    @Inject
    DiagramResourceService diagramResourceService;

    @Inject
    LineDao lineDao;

    @Transactional
    public Line createLine(String diagramId, String lineCatalogID, String source, String destination) {
        final Diagram diagram = diagramService.getDiagramById(diagramId);
        LineCatalog lc = lineCatalogService.getLineCatalogById(lineCatalogID);
        DiagramResource drSource = diagramResourceService.getDiagramResourceById(diagramId, source);
        DiagramResource drDest = diagramResourceService.getDiagramResourceById(diagramId, destination);
        Line l = new Line();
        l.setLineCatalog(lc);
        l.setSource(drSource);
        l.setDestination(drDest);
        l.setDiagram(diagram);

        return lineDao.create(diagramId, l);
    }

    public Line getLineById(String diagramId, String lineId) {
        return lineDao.getLineById(diagramId, lineId);
    }

    @Transactional
    public void deleteLineById(String diagramId, String lineId) {
        lineDao.deleteLineById(diagramId, lineId);
    }

    public List<Line> getLines(String diagramId) {
        return lineDao.getLines(diagramId);
    }
}
