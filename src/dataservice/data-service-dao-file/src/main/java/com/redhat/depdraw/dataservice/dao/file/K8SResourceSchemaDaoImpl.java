package com.redhat.depdraw.dataservice.dao.file;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.depdraw.dataservice.dao.api.K8SResourceSchemaDao;

@ApplicationScoped
public class K8SResourceSchemaDaoImpl implements K8SResourceSchemaDao {

    @Override
    public String getResourceCatalogById(String k8SResourceSchemaId) {
        try {
            Path fileName = Path.of(FileUtil.K8S_RESOURCE_SCHEMAS_FILES_DIR + k8SResourceSchemaId + ".json");
            return Files.readString(fileName);
        } catch (Exception e) {
            return null;
        }
    }
}
