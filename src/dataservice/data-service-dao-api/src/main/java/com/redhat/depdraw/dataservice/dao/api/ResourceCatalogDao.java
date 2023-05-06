package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.model.ResourceCatalog;

public interface ResourceCatalogDao {

    ResourceCatalog getResourceCatalogById(String resourceCatalogId);

    List<ResourceCatalog> getResourceCatalogs();
}
