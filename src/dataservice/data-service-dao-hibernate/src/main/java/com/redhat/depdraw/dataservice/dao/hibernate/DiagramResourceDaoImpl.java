package com.redhat.depdraw.dataservice.dao.hibernate;

import com.redhat.depdraw.dataservice.dao.api.DiagramResourceDao;
import com.redhat.depdraw.model.DiagramResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class DiagramResourceDaoImpl implements DiagramResourceDao {

    @Inject
    EntityManager em;

    @Override
    public DiagramResource create(String diagramId, DiagramResource dr) {
        em.persist(dr);

        return dr;
    }

    @Override
    public DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId) {
        return em.find(DiagramResource.class, diagramResourceId);
    }

    @Override
    public void deleteDiagramResourceById(String diagramId, String diagramResourceId) {
        DiagramResource dr = getDiagramResourceById(diagramId, diagramResourceId);

        em.remove(dr);
    }

    @Override
    public List<DiagramResource> getDiagramResources(String diagramId) {
        TypedQuery<DiagramResource> query = em.createNamedQuery("DiagramResource.findByDiagramId", DiagramResource.class);
        query.setParameter("id", diagramId);

        return query.getResultList();
    }

    @Override
    public void updateDefinition(String diagramId, String diagramResourceId, String definition) {
        DiagramResource dr = getDiagramResourceById(diagramId, diagramResourceId);

        dr.setDefinition(definition);

        em.merge(dr);
    }

    @Override
    public String getDefinition(String diagramId, String diagramResourceId) {
        return getDiagramResourceById(diagramId, diagramResourceId).getDefinition();
    }

    @Override
    public DiagramResource updateDiagramResource(String diagramId, DiagramResource dr) {
        return em.merge(dr);
    }
}
