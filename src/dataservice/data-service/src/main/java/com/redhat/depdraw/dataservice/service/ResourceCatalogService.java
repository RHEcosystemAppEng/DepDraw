package com.redhat.depdraw.dataservice.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.depdraw.dataservice.dao.api.ResourceCatalogDao;
import com.redhat.depdraw.dataservice.dao.model.ResourceCatalog;

@ApplicationScoped
public class ResourceCatalogService {

    @Inject
    ResourceCatalogDao resourceCatalogDao;

    public ResourceCatalog getResourceCatalogById(String resourceCatalogId) {
        return resourceCatalogDao.getResourceCatalogById(resourceCatalogId);
    }

    public List<ResourceCatalog> getResourceCatalogs() {
        return resourceCatalogDao.getResourceCatalogs();
    }
}
