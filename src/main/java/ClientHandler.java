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
    private Server serverReference = null;
    private PrintWriter pw = null;
    private Scanner scanner = null;
    private QueueHandler queueHandler = null;

    boolean keepRunning = false;

    public ClientHandler(int id, Socket socket, QueueHandler queueHandler) {
        this.id = id;
        this.socket = socket;
        this.queueHandler = queueHandler;

        try {
            this.scanner = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
            System.out.println("Client Handler Ready: " + id);
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

        try {
            switch (commandType) {
                case "CLOSE":
                    queueHandler.PutString("CLOSE#" + id + "#" + 0);
                    return false;

                case "SEND":
                    queueHandler.PutString("MESSAGE#" + commandValues[0] + "#" + commandValues[1]);
                    return true;
                default:
                    queueHandler.PutString("CLOSE#" + id + "#" + 1);
                    return false;
            }
        } catch (Exception e){
            queueHandler.PutString("CLOSE#"+id+"#"+2);
            return false;
        }
    }

    @Override
    public void run() {
        try {
            String message = "";
            //Connection
            try {
                System.out.println("Starting Connection for " + id + ": " + socket.getLocalAddress() + " : " + socket.getLocalPort());
                message = scanner.nextLine();
                System.out.println(message);
                String[] splitMsg = message.split("#");

                if(splitMsg[0].equals("CONNECT")){
                    name = splitMsg[1];
                    keepRunning = true;
                    pw.println("Du er forbundet, send en string for at få den uppercase, send 'stop' for at stoppe");
                    queueHandler.PutString("CONNECT#");
                } else
                    throw new Exception();
            } catch (Exception e){
                queueHandler.PutString("CLOSE#"+id+"#"+1);
            }

            //Commands
            while (keepRunning) {
                try {
                    message = scanner.nextLine();
                } catch (Exception e){
                    keepRunning = false;
                    queueHandler.PutString("CLOSE#"+id+"#"+2);
                    continue;
                }

                if (keepRunning)
                    keepRunning = HandleCommand(message);
            }

            socket.close();
        } catch (Exception e){
            queueHandler.PutString("CLOSE#"+id+"#"+2);
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
        pw.println(toPrint);
    }
}
