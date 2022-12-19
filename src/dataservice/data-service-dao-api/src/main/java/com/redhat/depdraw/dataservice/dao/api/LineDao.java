package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.model.Line;

public interface LineDao {
    Line create(Line l);

    Line getLineById(String diagramId, String lineId);

    void deleteLineById(String diagramId, String lineId);

    List<Line> getLines(String diagramId);

}
