import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {
    public static final  int DEFAULT_PORT = 8080;
    private static final List<ClientHandler> handlers = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private QueueHandler queueHandler = null;

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
        queueHandler = new QueueHandler(this);
        new Thread(queueHandler).start();
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

            ClientHandler clientHandler = new ClientHandler(id, socket, this);
            new Thread(clientHandler).start();
            handlers.add(clientHandler);
        }
    }

    public void ServerCommands(String command){
        String[] commandSplit = command.split("#"), commandValues = new String[commandSplit.length - 1];
        String commandType = commandSplit[0];
        for(int i = 1; i < commandValues.length; i++)
            commandValues[i - 1] = commandSplit[i];

        if(commandType.equals("ONLINE")){
            String allMembers = "ONLINE#";
            for (int i = 0; i < handlers.size(); i++){
                allMembers += handlers.get(i).getName();

                if(i != handlers.size() - 1)
                    allMembers += ",";
            }
            System.out.println("C: " + allMembers);
            for (ClientHandler handler: handlers)
                handler.PrintString(allMembers);
        } else if(commandType.equals("CLOSE")){
            ClientHandler toDelete = null;
            for (ClientHandler handler: handlers){
                try {
                    if (handler.getId() == Integer.parseInt(commandValues[0])) {
                        toDelete = handler;
                        handler.CloseConnection(Integer.parseInt(commandValues[1]));
                        while (handler.isConnected()) {}
                    }
                } catch (Exception e){
                }
            }
            if(toDelete != null){
                handlers.remove(toDelete);
            }
        } else if(commandType.equals("MESSAGE")){
            String[] receivers = commandValues[0].split(",");

            for (ClientHandler handler: handlers) {
                for(String name: receivers) {
                    if (handler.getName().equals(name)) {
                        handler.PrintString(commandValues[1]);
                    }
                }
            }
        }
    }
}
