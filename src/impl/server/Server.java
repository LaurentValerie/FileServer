package impl.server;

import impl.utils.Request;
import impl.utils.Response;
import impl.utils.ResponseType;

import static impl.utils.RequestType.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    public void run(int PORT) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            FileStorage.load();
            System.out.println("Server started!");
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Request request = new Request(NULL);
            while (request.getType() != EXIT) {
                try (Socket socket = server.accept();
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())
                ) {
                    request = (Request) input.readObject();
                    if (request.getType() != EXIT) {
                        Future<Response> future = executor.submit(new FileManager(request));
                        Response response = future.get();
                        output.writeObject(response);
                        output.flush();
                    } else {
                        output.writeObject(new Response(ResponseType.EXIT_SUCCESS));
                    }
                } catch (ClassNotFoundException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            FileStorage.save();
            System.out.println("Server is turned off");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
