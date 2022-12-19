package com.redhat.depdraw.dataservice.dao.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.depdraw.dataservice.dao.api.LineDao;
import com.redhat.depdraw.dataservice.dao.model.Line;

@ApplicationScoped
public class LineDaoImpl implements LineDao {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Line create(Line l) {
        try {
            final String s = objectMapper.writeValueAsString(l);
            final String pathString = FileUtil.DIAGRAM_FILES_DIR + l.getDiagramID() + "/" + FileUtil.LINE_FILES_DIR + l.getUuid() + "/";
            Path path = Path.of(pathString);

            Files.createDirectories(path);

            Path fileName = Path.of(pathString + FileUtil.LINE_FILE_NAME);

            // Writing into the file
            Files.writeString(fileName, s, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            return l;
        }

        return l;
    }

    @Override
    public Line getLineById(String diagramId, String lineId) {
        try {
            Path fileName = Path.of(FileUtil.DIAGRAM_FILES_DIR + diagramId
                    + "/" + FileUtil.LINE_FILES_DIR + lineId
                    + "/" + FileUtil.LINE_FILE_NAME);
            final String s = Files.readString(fileName);
            return objectMapper.readValue(s, Line.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteLineById(String diagramId, String lineId) {
        FileUtil.deleteDirectory(new File(FileUtil.DIAGRAM_FILES_DIR + diagramId
                + "/" + FileUtil.LINE_FILES_DIR + lineId));
    }

    @Override
    public List<Line> getLines(String diagramId) {
        Path diagramPath = Path.of(FileUtil.DIAGRAM_FILES_DIR + diagramId
                + "/" + FileUtil.LINE_FILES_DIR);
        try(final Stream<Path> paths = Files.list(diagramPath)){
            return paths.map(p -> getLineById(diagramId, p.getFileName().toString())).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return List.of();
    }

}
