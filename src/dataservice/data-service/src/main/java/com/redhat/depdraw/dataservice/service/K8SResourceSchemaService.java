package com.redhat.depdraw.dataservice.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.depdraw.dataservice.dao.api.K8SResourceSchemaDao;

@ApplicationScoped
public class K8SResourceSchemaService {

    @Inject
    K8SResourceSchemaDao k8SResourceSchemaDao;

    public String getK8sResourceSchemaById(String k8sResourceSchemaId) {
        return k8SResourceSchemaDao.getResourceCatalogById(k8sResourceSchemaId);
    }
}
