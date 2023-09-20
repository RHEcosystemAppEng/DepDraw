package com.redhat.depdraw.dataservice.dao.hibernate;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.api.LineCatalogDao;
import com.redhat.depdraw.model.LineCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class LineCatalogDaoImpl implements LineCatalogDao {

    @Inject
    EntityManager em;

    @Override
    public LineCatalog getLineCatalogById(String lineCatalogId) {
        return em.find(LineCatalog.class, lineCatalogId);
    }

    @Override
    public List<LineCatalog> getLineCatalogs() {
        TypedQuery<LineCatalog> query = em.createNamedQuery("LineCatalog.findAll", LineCatalog.class);

        return query.getResultList();
    }

}
