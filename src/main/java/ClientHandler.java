import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    boolean connected = false;

    private int id = -1;
    private String name = "";
    private Socket socket = null;
    private Server serverReference = null;
    private PrintWriter pw = null;
    private Scanner scanner = null;

    public ClientHandler(int id, Socket socket) {
        this.id = id;
        this.name = name;
        this.socket = socket;
        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e){
        }
    }

    public boolean HandleCommand(String command){
        String[] commandSplit = command.split("#");
        String commandType = commandSplit[0];
        String[] commandValues = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++)
            commandValues[i - 1] = commandSplit[i];

        if(!connected){
            if(commandType.equals("CONNECT") && commandValues.length > 0) {
                connected = true;
                name = commandValues[0];
                return true;
            }

            return false;
        }

        switch (commandType){
            case "CLOSE":
                pw.println("Closing Your Connection");
                return false;

            case "CONNECT":

                return true;

            case "SEND":

                return true;

            default:
                return false;
        }
    }

    @Override
    public void run() {
        try {
            pw.println("Du er forbundet, send en string for at få den uppercase, send 'stop' for at stoppe");
            String message = "";
            boolean keepRunning = true;
            while (keepRunning) {
                if (keepRunning)
                    keepRunning = HandleCommand(message);
            }

            pw.println("Forbindelsen lukker");
            socket.close();
        } catch (Exception e){ }
    }

    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive(){
        return socket.isConnected();
    }
    //endregion
}
