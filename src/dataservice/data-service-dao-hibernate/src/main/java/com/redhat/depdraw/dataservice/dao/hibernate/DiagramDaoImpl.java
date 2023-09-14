package com.redhat.depdraw.dataservice.dao.hibernate;

import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.model.Diagram;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class DiagramDaoImpl implements DiagramDao {

    @Inject
    EntityManager em;

    @Override
    public Diagram create(Diagram diagram) {
        em.persist(diagram);

        return diagram;
    }

    @Override
    public Diagram getDiagramById(String diagramId) {
        return em.find(Diagram.class, diagramId);
    }

    @Override
    public void deleteDiagramById(String diagramId) {
        Diagram d = getDiagramById(diagramId);

        em.remove(d);
    }

    @Override
    public List<Diagram> getDiagrams() {
        TypedQuery<Diagram> query = em.createNamedQuery("Diagram.findAll", Diagram.class);

        return query.getResultList();
    }

    @Override
    public Diagram updateDiagram(Diagram diagram) {
        return em.merge(diagram);
    }
}
