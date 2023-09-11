package com.redhat.depdraw.dataservice.service;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.dataservice.dao.api.LineDao;
import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    @Inject
    DiagramDao diagramDao;


    public Line createLine(String diagramId, String lineCatalogID, String source, String destination) {
        LineCatalog lc = lineCatalogService.getLineCatalogById(lineCatalogID);
        DiagramResource drSource = diagramResourceService.getDiagramResourceById(diagramId, source);
        DiagramResource drDest = diagramResourceService.getDiagramResourceById(diagramId, destination);

        final Line createdLine = lineDao.create(diagramId, lc, drSource, drDest);

        final Diagram diagram = diagramService.getDiagramById(diagramId);
        diagram.getLines().add(createdLine);

        diagramDao.updateDiagram(diagram);

        return createdLine;
    }

    public Line getLineById(String diagramId, String lineId) {
        return lineDao.getLineById(diagramId, lineId);
    }

    public void deleteLineById(String diagramId, String lineId) {
        final Line line = getLineById(diagramId, lineId);

        if(line != null) {
            lineDao.deleteLineById(diagramId, lineId);
            final Diagram diagram = diagramService.getDiagramById(diagramId);
            if(diagram != null){
                diagram.getLines().remove(line);
            }

            diagramDao.updateDiagram(diagram);
        }
    }

    public List<Line> getLines(String diagramId) {
        return lineDao.getLines(diagramId);
    }
}
