import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    public static final  int DEFAULT_PORT = 2345;
    private static final List<ClientHandler> handlers = new ArrayList<>();
    private ServerSocket serverSocket = null;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if(args.length > 0){
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e){
                System.out.println("Invalid port number, using default port: " + DEFAULT_PORT);
            }
        }

        new Server().StartServer(port);
    }

    public void StartServer(int port) throws Exception{
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on: " + port);
        while (true) {
            System.out.println("Waiting for a Client");
            Socket socket = serverSocket.accept();
            System.out.println("New Client connected\n");
            int id = -1;
            for(int i = 0; i < 100_000; i++){
                boolean check = true;
                for (ClientHandler handler: handlers) {
                    if(handler.getId() == i) {
                        check = false;
                        break;
                    }
                }

                if(check) {
                    id = i;
                    break;
                }
            }

            ClientHandler clientHandler = new ClientHandler(id, socket);
            new Thread(clientHandler).start();
            handlers.add(clientHandler);
        }
    }

    public void Commands(String command){
        if(command.equals("ONLINE")){
            String allMembers = "ONLINE#";
            for (int i = 0; i < handlers.size(); i++){
                allMembers += handlers.get(i).getName();

                if(i != handlers.size() - 1)
                    allMembers += ",";
            }

            for (ClientHandler handler: handlers)
                handler.HandleCommand(allMembers);
        }
    }
}
