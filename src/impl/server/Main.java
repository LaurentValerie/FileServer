package impl.server;


public class Main {
    private static final int PORT = 34522;


    public static void main(String[] args) {
        Server server = new Server();
        server.run(PORT);
    }
}