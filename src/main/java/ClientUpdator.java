import java.net.Socket;
import java.util.Scanner;

public class ClientUpdator implements Runnable{
    private Socket socket = null;
    private Client client = null;
    private Scanner scanner = null;
    public boolean keepRunning = true;

    public ClientUpdator(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
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

                String[] split = s.split("#");
                if(split.length > 0){
                    if(split[0].equals("CLOSE"))
                        End();
                }
            } catch (Exception e){
            }
        }
    }

    public void End(){
        keepRunning = false;
        client.End();
    }
}
