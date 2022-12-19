package com.redhat.depdraw.dataservice.dao.file;

import java.io.File;

public class FileUtil {
    public static final String BASE_FILES_DIR = "~/opt/DepDraw/data/";
    public static final String DIAGRAM_FILES_DIR = BASE_FILES_DIR + "Diagrams/";
    public static final String DIAGRAM_FILE_NAME = "Diagram.json";
    public static final String DIAGRAM_RESOURCE_FILE_NAME = "DiagramResource.json";
    public static final String DIAGRAM_RESOURCE_DEFINITION_FILE_NAME = "Definition.json";
    public static final String LINE_FILE_NAME = "Line.json";
    public static final String LINE_FILES_DIR = "Lines/";
    public static final String DIAGRAM_RESOURCES_FILES_DIR = "Resources/";
    public static final String K8S_RESOURCE_SCHEMAS_FILES_DIR = BASE_FILES_DIR + "K8SResourceSchemas/";
    public static final String RESOURCE_CATALOG_FILES_DIR = BASE_FILES_DIR + "ResourceCatalogs/";
    public static final String LINE_CATALOG_FILES_DIR = BASE_FILES_DIR + "LineCatalogs/";

    private FileUtil() {
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

}
