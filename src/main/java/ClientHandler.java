import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientHandler implements Runnable{
    boolean connected = false;

    private int id = -1;
    private String name = "";
    private Socket socket = null;
    private PrintWriter pw = null;
    private Scanner scanner = null;
    private Server server;

    boolean keepRunning = true;

    public ClientHandler(int id, Socket socket, Server server) {
        this.id = id;
        this.socket = socket;
        this.server = server;

        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e){
        }
    }

    public boolean HandleCommand(String command) {
        String[] commandSplit = command.split("#");
        String commandType = commandSplit[0];
        String[] commandValues = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++)
            commandValues[i - 1] = commandSplit[i];

        if (!connected) {
            if (commandType.equals("CONNECT")) {
                connected = true;
                name = commandValues[0];
                server.ServerCommands("ONLINE#");
                return true;
            }

            return false;
        } else {
            if (commandType.equals("CLOSE")) {
                server.ServerCommands("CLOSE#" + id + "#" + 0);
                return false;
            } else if (commandType.equals("SEND")) {
                server.ServerCommands("MESSAGE#" + commandValues[0] + "#" + commandValues[1]);
                return true;
            } else {
                server.ServerCommands("CLOSE#" + id + "#" + 1);
                return false;
            }
        }
    }

    @Override
    public void run() {
        try {
            String message = "";
            //Commands
            while (keepRunning) {
                try {
                    message = scanner.nextLine();
                    System.out.println(id + ": " + message);
                } catch (Exception e){
                    keepRunning = false;
                    server.ServerCommands("CLOSE#"+id+"#"+2);
                    continue;
                }

                if (keepRunning)
                    keepRunning = HandleCommand(message);
            }

            socket.close();
        } catch (Exception e){
            e.printStackTrace();
            server.ServerCommands("CLOSE#"+id+"#"+2);
        }
    }

    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isConnected(){
        return socket.isConnected();
    }
    //endregion

    public void CloseConnection(int i) {
        try {
            pw.println("Forbindelsen lukker: \nCLOSE#" + i);
            keepRunning = false;
            socket.close();
        } catch (Exception e){
        }
    }

    public void PrintString(String toPrint){
        try{
            pw.println(toPrint);
            System.out.println("Print for " + id + ": " + toPrint);
        } catch (Exception e){
            System.out.println("Failed");
        }
    }
}
