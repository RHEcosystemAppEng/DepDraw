package com.redhat.depdraw.dataservice.dao.hibernate;


import com.redhat.depdraw.dataservice.dao.api.K8SResourceSchemaDao;
import com.redhat.depdraw.model.K8SResourceSchema;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class K8SResourceSchemaDaoImpl implements K8SResourceSchemaDao {

    @Inject
    EntityManager em;

    @Override
    public String getK8sResourceSchemaById(String k8SResourceSchemaId) {
        K8SResourceSchema k8SResourceSchema = em.find(K8SResourceSchema.class, k8SResourceSchemaId);

        return k8SResourceSchema.getData();
    }
}
