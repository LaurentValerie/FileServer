package impl.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static byte[] readContent(String fileStorage, String sourceFileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileStorage + sourceFileName));
    }


    public static void writeContent(String fileStorage, String filename, byte[] content) throws IOException {
        Path path = Paths.get(fileStorage + filename);
        Path newFile = Files.createFile(path);
        Files.write(newFile, content);
    }
}
