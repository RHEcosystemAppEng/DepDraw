package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;

public interface LineDao {
    Line create(String diagramId, Line line);

    Line getLineById(String diagramId, String lineId);

    void deleteLineById(String diagramId, String lineId);

    List<Line> getLines(String diagramId);

}
