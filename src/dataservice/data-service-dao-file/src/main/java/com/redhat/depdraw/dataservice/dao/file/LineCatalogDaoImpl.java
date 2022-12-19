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
import com.redhat.depdraw.dataservice.dao.api.LineCatalogDao;
import com.redhat.depdraw.dataservice.dao.model.LineCatalog;

@ApplicationScoped
public class LineCatalogDaoImpl implements LineCatalogDao {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public LineCatalog getLineCatalogById(String lineCatalogId) {
        try {
            Path fileName = Path.of(FileUtil.LINE_CATALOG_FILES_DIR + lineCatalogId + ".json");
            final String s = Files.readString(fileName);
            return objectMapper.readValue(s, LineCatalog.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<LineCatalog> getLineCatalogs() {
        Path path = Path.of(FileUtil.LINE_CATALOG_FILES_DIR);
        try(final Stream<Path> paths = Files.list(path)){
            return paths.map(p -> getLineCatalogById(p.getFileName().toString().replace(".json", ""))).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return List.of();
    }

}
