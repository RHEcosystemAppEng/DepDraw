package com.redhat.depdraw.dataservice.dao.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.depdraw.dataservice.dao.api.ResourceCatalogDao;
import com.redhat.depdraw.dataservice.dao.model.ResourceCatalog;

@ApplicationScoped
public class ResourceCatalogDaoImpl implements ResourceCatalogDao {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public ResourceCatalog getResourceCatalogById(String resourceCatalogId) {
        try {
            Path fileName = Path.of(FileUtil.RESOURCE_CATALOG_FILES_DIR + resourceCatalogId + ".json");
            final String s = Files.readString(fileName);
            return objectMapper.readValue(s, ResourceCatalog.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ResourceCatalog> getResourceCatalogs() {
        Path path = Path.of(FileUtil.RESOURCE_CATALOG_FILES_DIR);
        try(final Stream<Path> paths = Files.list(path)){
            return paths.map(p -> getResourceCatalogById(p.getFileName().toString().replace(".json", ""))).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return List.of();
    }
}
