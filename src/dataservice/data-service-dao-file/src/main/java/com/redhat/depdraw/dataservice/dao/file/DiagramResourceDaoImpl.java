package com.redhat.depdraw.dataservice.dao.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.depdraw.dataservice.dao.api.DiagramResourceDao;
import com.redhat.depdraw.model.DiagramResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiagramResourceDaoImpl implements DiagramResourceDao {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public DiagramResource create(String diagramId, DiagramResource dr) {
        String uuid = UUID.randomUUID().toString();
        dr.setUuid(uuid);

        return createInternal(diagramId, dr, StandardOpenOption.CREATE_NEW);
    }

    private DiagramResource createInternal(String diagramId, DiagramResource dr, OpenOption... openOptions) {
        try {
            final String s = objectMapper.writeValueAsString(dr);
            final String pathString = FileUtil.DIAGRAM_FILES_DIR + diagramId + "/" + FileUtil.DIAGRAM_RESOURCES_FILES_DIR + dr.getUuid() + "/";
            Path path = Path.of(pathString);

            Files.createDirectories(path);

            Path fileName = Path.of(pathString + FileUtil.DIAGRAM_RESOURCE_FILE_NAME);

            // Writing into the DiagramResource file
            Files.writeString(fileName, s, openOptions);

            fileName = Path.of(pathString + FileUtil.DIAGRAM_RESOURCE_DEFINITION_FILE_NAME);

            // Writing into the DiagramResourceDefinition file
            Files.writeString(fileName, "", openOptions);
        } catch (IOException e) {
            return dr;
        }

        return dr;
    }

    @Override
    public DiagramResource getDiagramResourceById(String diagramId, String diagramResourceId) {
        try {
            Path fileName = Path.of(FileUtil.DIAGRAM_FILES_DIR + diagramId
                    + "/" + FileUtil.DIAGRAM_RESOURCES_FILES_DIR + diagramResourceId
                    + "/" + FileUtil.DIAGRAM_RESOURCE_FILE_NAME);
            final String s = Files.readString(fileName);
            return objectMapper.readValue(s, DiagramResource.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteDiagramResourceById(String diagramId, String diagramResourceId) {
        FileUtil.deleteDirectory(new File(FileUtil.DIAGRAM_FILES_DIR + diagramId
                + "/" + FileUtil.DIAGRAM_RESOURCES_FILES_DIR + diagramResourceId));
    }

    @Override
    public List<DiagramResource> getDiagramResources(String diagramId) {
        Path path = Path.of(FileUtil.DIAGRAM_FILES_DIR + diagramId
                + "/" + FileUtil.DIAGRAM_RESOURCES_FILES_DIR);
        try(final Stream<Path> paths = Files.list(path)){
            return paths.map(p -> getDiagramResourceById(diagramId, p.getFileName().toString())).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return List.of();
    }


    @Override
    public void updateDefinition(String diagramId, String diagramResourceId, String definition) {
        try {
            final String diagramResourcePathString = FileUtil.DIAGRAM_FILES_DIR + diagramId + "/"
                    + FileUtil.DIAGRAM_RESOURCES_FILES_DIR + diagramResourceId + "/";
            Path fileName = Path.of(diagramResourcePathString + FileUtil.DIAGRAM_RESOURCE_DEFINITION_FILE_NAME);
            Files.writeString(fileName, definition, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.out.println("error" + "\n" + definition + "\n" + diagramId + "\n" + diagramResourceId);
        }
    }

    @Override
    public String getDefinition(String diagramId, String diagramResourceId) {
        try {
            final String diagramResourcePathString = FileUtil.DIAGRAM_FILES_DIR + diagramId + "/"
                    + FileUtil.DIAGRAM_RESOURCES_FILES_DIR + diagramResourceId + "/";
            Path fileName = Path.of(diagramResourcePathString + FileUtil.DIAGRAM_RESOURCE_DEFINITION_FILE_NAME);
            return Files.readString(fileName);
        } catch (Exception e) {
            System.out.println("error" + "\n"  + "\n" + diagramId + "\n" + diagramResourceId);
            return null;
        }
    }

    @Override
    public DiagramResource updateDiagramResource(String diagramId, DiagramResource dr) {
        return createInternal(diagramId, dr, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
