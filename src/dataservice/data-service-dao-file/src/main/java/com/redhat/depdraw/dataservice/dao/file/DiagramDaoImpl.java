package com.redhat.depdraw.dataservice.dao.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.depdraw.dataservice.dao.api.DiagramDao;
import com.redhat.depdraw.dataservice.dao.model.Diagram;

@ApplicationScoped
public class DiagramDaoImpl implements DiagramDao {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Diagram create(Diagram d) {
        return createInternal(d, StandardOpenOption.CREATE_NEW);
    }

    private Diagram createInternal(Diagram d, OpenOption... openOptions) {
        try {
            final String s = objectMapper.writeValueAsString(d);
            final String pathString = FileUtil.DIAGRAM_FILES_DIR + d.getUuid() + "/";
            Path path = Path.of(pathString);

            Files.createDirectories(path);

            Path fileName = Path.of(pathString + FileUtil.DIAGRAM_FILE_NAME);

            // Writing into the file
            Files.writeString(fileName, s, openOptions);
        } catch (IOException e) {
            return d;
        }

        return d;
    }

    @Override
    public Diagram getDiagramById(String diagramId) {
        try {
            Path fileName = Path.of(FileUtil.DIAGRAM_FILES_DIR + diagramId
                    + "/" + FileUtil.DIAGRAM_FILE_NAME);
            final String s = Files.readString(fileName);
            return objectMapper.readValue(s, Diagram.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteDiagramById(String diagramId) {
        FileUtil.deleteDirectory(new File(FileUtil.DIAGRAM_FILES_DIR + diagramId + "/"));
    }

    @Override
    public List<Diagram> getDiagrams() {
        Path diagramPath = Path.of(FileUtil.DIAGRAM_FILES_DIR);
        try(final Stream<Path> paths = Files.list(diagramPath)){
            return paths.map(p -> getDiagramById(p.getFileName().toString())).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return List.of();
    }

    @Override
    public Diagram updateDiagram(Diagram diagram) {
        return createInternal(diagram, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
