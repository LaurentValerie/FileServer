package impl.client;


import impl.server.FileStorage;

import java.io.File;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) {
        Client client = new Client();
        client.run(ADDRESS, PORT);
    }
}
