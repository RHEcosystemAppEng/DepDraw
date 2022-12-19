package com.redhat.depdraw.dataservice.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.depdraw.dataservice.dao.api.LineCatalogDao;
import com.redhat.depdraw.dataservice.dao.model.LineCatalog;

@ApplicationScoped
public class LineCatalogService {

    @Inject
    LineCatalogDao lineCatalogDao;

    public LineCatalog getLineCatalogById(String lineCatalogId) {
        return lineCatalogDao.getLineCatalogById(lineCatalogId);
    }

    public List<LineCatalog> getLineCatalogs() {
        return lineCatalogDao.getLineCatalogs();
    }
}
