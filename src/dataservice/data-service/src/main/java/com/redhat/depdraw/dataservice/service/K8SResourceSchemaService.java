package com.redhat.depdraw.dataservice.service;

import com.redhat.depdraw.dataservice.dao.api.K8SResourceSchemaDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class K8SResourceSchemaService {

    @Inject
    K8SResourceSchemaDao k8SResourceSchemaDao;

    public String getK8sResourceSchemaById(String k8sResourceSchemaId) {
        return k8SResourceSchemaDao.getK8sResourceSchemaById(k8sResourceSchemaId);
    }
}
