package impl.client;

import impl.utils.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static impl.utils.ConsoleReader.*;
import static impl.utils.RequestType.*;


public class Client {
    private static final String fileStorage = System.getProperty("user.dir") +
            File.separator + "client" + File.separator + "data" + File.separator;


    public void run(String ADDRESS, int PORT) {
        try (Socket socket = new Socket(ADDRESS, PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            output.flush();
            System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
            RequestType requestType = getRequest();
            Request request = formRequest(requestType);
            output.writeObject(request);
            output.flush();
            TimeUnit.MILLISECONDS.sleep(50); // Need to wait response
            Response response = (Response) input.readObject();
            processResponse(response, requestType);
        } catch (ConnectException e) {
            System.out.println("You should start server first");
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private Request formRequest(RequestType requestType) {
        return switch (requestType) {
            case PUT -> formPutRequest(getFileName("Enter filename: "));
            case GET -> formGetRequest();
            case DELETE -> formDeleteRequest();
            case EXIT -> new Request(EXIT);
            case BY_ID, BY_NAME, NULL -> null;
        };
    }


    private void processResponse(Response response, RequestType requestType) {
        ResponseType responseType = response.getType();
        switch (responseType) {
            case SUCCESS -> {
                switch (requestType) {
                    case PUT -> System.out.println("Response says that file is saved! ID = " + response.getID());
                    case DELETE -> System.out.println("The response says that the file was successfully deleted!");
                    case GET -> {
                        String filename = getFileName("The file was downloaded! Specify a name for it: ");
                        byte[] content = response.getContent();
                        try {
                            FileHelper.writeContent(fileStorage, filename, content);
                            System.out.println("File saved on the hard drive!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case FORBIDDEN -> System.out.println("The response says that creating the file was forbidden!");
            case NOT_EXIST -> System.out.println("The response says that this file is not found!");
            case EXIT_SUCCESS -> System.out.println("Server shutdown request successfully sent");
        }
    }


    private Request formPutRequest(String sourceFileName) {
        Request result;
        byte[] content = new byte[0];
        try {
            content = FileHelper.readContent(fileStorage, sourceFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newFileName = getFileName("Enter name of the file to be saved on server: ");
        newFileName = newFileName.equals("") ? sourceFileName: newFileName;
        result = new Request(PUT, newFileName);
        result.setContent(content);
        System.out.println("The request was sent.");
        return result;
    }

    private Request formGetRequest() {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        if (byNameOrId() == BY_NAME) {
            String filename = getFileName("Enter filename: ");
            System.out.println("The request was sent.");
            return new Request(GET, BY_NAME, filename);
        } else {
            System.out.print("Enter id: ");
            int id = readInt();
            System.out.println("The request was sent.");
            return new Request(GET, BY_ID, id);
        }
    }

    private Request formDeleteRequest() {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        if (byNameOrId() == BY_NAME) {
            String filename = getFileName("Enter filename: ");
            System.out.println("The request was sent.");
            return new Request(DELETE, BY_NAME, filename);
        } else {
            System.out.print("Enter id: ");
            int id = readInt();
            System.out.println("The request was sent.");
            return  new Request(DELETE, BY_ID, id);
        }
    }

    private String getFileName(String outputMessage) {
        System.out.print(outputMessage);
        String sourceFileName = null;
        while (sourceFileName == null) {
            sourceFileName = readString();
        }
        return sourceFileName;
    }

}
