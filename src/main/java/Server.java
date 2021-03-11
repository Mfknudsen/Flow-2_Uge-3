import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final  int DEFAULT_PORT = 8080;
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
                    System.out.println(i);

                    ClientHandler clientHandler = new ClientHandler(id, socket, this);
                    new Thread(clientHandler).start();
                    handlers.add(clientHandler);

                    break;
                }
            }
        }
    }

    public synchronized void ServerCommands(String name, String command){
        String[] commandSplit = command.split("#");
        String[] commandValues = new String[commandSplit.length - 1];
        String commandType = commandSplit[0];
        for(int i = 1; i < commandSplit.length; i++)
            commandValues[i - 1] = commandSplit[i];

        if(commandType.equals("ONLINE")){
            String allMembers = "ONLINE#";
            for (int i = 0; i < handlers.size(); i++){
                allMembers += handlers.get(i).getName();

                if(i != handlers.size() - 1)
                    allMembers += ",";
            }
            for (ClientHandler handler: handlers)
                handler.PrintString(allMembers);

        } else if(commandType.equals("CLOSE")){
            ClientHandler toDelete = null;
            for (ClientHandler handler: handlers){
                try {
                    if (handler.getId() == Integer.parseInt(commandValues[0])) {
                        toDelete = handler;
                        handler.PrintString("CLOSE#" + commandValues[1]);
                        handler.CloseConnection(Integer.parseInt(commandValues[1]));
                        System.out.println("CLOSE#" + commandValues[1]);
                        Thread.sleep(1);
                    }
                } catch (Exception e){
                }
            }
            if(toDelete != null)
                handlers.remove(toDelete);
            ServerCommands("","ONLINE#");

        } else if(commandType.equals("MESSAGE")){
            String[] receivers = commandValues[0].split(",");

            for (String s: receivers)
                System.out.println(s);

            boolean all = false;
            for (String s: receivers){
                if(s.equals("*"))
                    all = true;
            }

            for (ClientHandler handler: handlers) {
                if(!all) {
                    for (String n : receivers) {
                        if (handler.getName().equals(n)) {
                            handler.PrintString("MESSAGE#" + name + "#" + commandValues[1]);
                        }
                    }
                }
                else {
                    handler.PrintString("MESSAGE#" + name + "#" + commandValues[1]);
                }
            }
        }
    }
}
