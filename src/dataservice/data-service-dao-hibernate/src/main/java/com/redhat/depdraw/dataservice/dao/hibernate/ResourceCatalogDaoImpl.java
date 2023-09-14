package com.redhat.depdraw.dataservice.dao.hibernate;

import com.redhat.depdraw.dataservice.dao.api.ResourceCatalogDao;
import com.redhat.depdraw.model.ResourceCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class ResourceCatalogDaoImpl implements ResourceCatalogDao {

    @Inject
    EntityManager em;

    @Override
    public ResourceCatalog getResourceCatalogById(String resourceCatalogId) {
        return em.find(ResourceCatalog.class, resourceCatalogId);
    }

    @Override
    public List<ResourceCatalog> getResourceCatalogs() {
        TypedQuery<ResourceCatalog> query = em.createNamedQuery("ResourceCatalog.findAll", ResourceCatalog.class);

        return query.getResultList();
    }
}
