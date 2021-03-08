import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private int id = -1;
    private String name = "";
    private Socket socket = null;
    private PrintWriter pw = null;
    private Scanner scanner = null;

    public ClientHandler(int id, Socket socket) {
        this.id = id;
        this.socket = socket;

        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean HandleCommand(String command){

        return false;
    }

    @Override
    public void run() {
        try {
            pw.println("Du er forbundet, send en string for at få den uppercase, send 'stop' for at stoppe");
            String message = "";
            boolean keepRunning = true;
            while (keepRunning) {
                if (!name.equals("")) {
                    try {
                        message = scanner.nextLine();
                    } catch (Exception e) {
                        keepRunning = false;
                        continue;
                    }

                    if (keepRunning)
                        keepRunning = HandleCommand(message);
                }
                else{
                    pw.println("Type In Account Name");
                    name = scanner.nextLine();
                }
            }

            pw.println("Forbindelsen lukker");
            socket.close();
        } catch (Exception e){ }
    }

    //region Getters
    public int getId() {
        return id;
    }
    //endregion
}
