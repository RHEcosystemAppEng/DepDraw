package com.redhat.depdraw.dataservice.dao.api;

import java.util.List;

import com.redhat.depdraw.dataservice.dao.model.LineCatalog;

public interface LineCatalogDao {

    LineCatalog getLineCatalogById(String lineCatalogId);

    List<LineCatalog> getLineCatalogs();
}
