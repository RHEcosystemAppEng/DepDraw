package com.redhat.depdraw.dataservice.dao.hibernate;

import com.redhat.depdraw.dataservice.dao.api.LineDao;
import com.redhat.depdraw.model.Line;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class LineDaoImpl implements LineDao {

    @Inject
    EntityManager em;

    @Override
    public Line create(String diagramId, Line line) {
        em.persist(line);

        return line;
    }

    @Override
    public Line getLineById(String diagramId, String lineId) {
        return em.find(Line.class, lineId);
    }

    @Override
    public void deleteLineById(String diagramId, String lineId) {
        Line l = getLineById(diagramId, lineId);

        em.remove(l);
    }

    @Override
    public List<Line> getLines(String diagramId) {
        TypedQuery<Line> query = em.createNamedQuery("Line.findByDiagramId", Line.class);
        query.setParameter("id", diagramId);

        return query.getResultList();
    }

}
