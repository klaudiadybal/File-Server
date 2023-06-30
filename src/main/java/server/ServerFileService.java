package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerFileService {
    private static final String DIRECTORY_PATH = "C:\\Users\\klaud\\IdeaProjects\\File-Server\\src\\main\\java\\server\\data";

    public static String putFileData(String fileName, String content) throws IOException {
        Path filePath = Paths.get(DIRECTORY_PATH, fileName);
        Path directoryPath = Paths.get(DIRECTORY_PATH);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        if(Files.exists(filePath)) {
            return "403";
        }

        Files.write(filePath, content.getBytes());
        return "200";
    }

    public static String getFileData(String fileName) throws IOException {
        Path filePath = Paths.get(DIRECTORY_PATH, fileName);
        if(!Files.exists(filePath)) {
            return "404";
        }
        return new String(Files.readAllBytes(filePath));
    }

    public static String deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(DIRECTORY_PATH, fileName);
        if(!Files.exists(filePath)) {
            return "404";
        }
        Files.delete(filePath);
        return "200";
    }
}
