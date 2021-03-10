import java.net.Socket;
import java.util.Scanner;

public class ClientUpdator implements Runnable{
    private Socket socket = null;
    private Scanner scanner = null;
    private boolean keepRunning = true;

    public ClientUpdator(Socket socket) {
        this.socket = socket;
        try {
            this.scanner = new Scanner(socket.getInputStream());
        } catch (Exception e){
        }
    }

    @Override
    public void run() {
        while (keepRunning){
            try {
                String s = scanner.nextLine();
                System.out.println(s);
            } catch (Exception e){
            }
        }
    }

    public void End(){
        keepRunning = false;
    }
}
