package impl.server;


import impl.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import static impl.utils.RequestType.*;
import static impl.utils.ResponseType.*;

public class FileManager implements Callable<Response> {

    private final String fileStorage = FileStorage.getStoragePath();
    private final Request request;

    public FileManager(Request request) {
        this.request = request;
    }


    public Response processRequest() {
        RequestType type = request.getType();
        return switch (type) {
            case PUT -> put(request);
            case GET -> get(request);
            case DELETE -> delete(request);
            default -> null;
        };
    }

    static int getFileID(String fileName) {
        return fileName.hashCode();
    }

    private Response put(Request request) {
        String filename = request.getFilename();
        byte[] content = request.getContent();
        int id = getFileID(filename);
        if (filename.isEmpty()) {
            filename = "file" + id;
        }
        FileStorage.addToFilesMap(id, filename);
        try {
            FileHelper.writeContent(fileStorage, filename, content);
            return new Response(SUCCESS, id);
        } catch (IOException e) {
            return new Response(FORBIDDEN);
        }
    }

    private Response delete(Request request) {
        String filename;
        if (request.getGetBy() == BY_NAME) {
            filename = request.getFilename();
            FileStorage.remove(filename);
        } else {
            filename = FileStorage.getFileNameByID(request.getId());
            FileStorage.remove(request.getId());
        }
        Path path = Path.of(fileStorage + filename);
        try {
            if (Files.deleteIfExists(path)) {
                return new Response(SUCCESS);
            } else {
                return new Response(NOT_EXIST);
            }
        } catch (IOException e) {
            return new Response(NOT_EXIST);
        }
    }

    private Response get(Request request) {
        String filename;
        if (request.getGetBy() == BY_NAME) {
            filename = request.getFilename();
        } else {
            filename = FileStorage.getFileNameByID(request.getId());
        }
        try {
            byte[] content = FileHelper.readContent(fileStorage, filename);
            Response response = new Response(SUCCESS);
            response.setContent(content);
            return response;
        } catch (IOException e) {
            return new Response(NOT_EXIST);
        }
    }


    @Override
    public Response call() {
        return processRequest();
    }
}
