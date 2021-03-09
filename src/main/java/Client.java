import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner keyboardScanner = new Scanner(System.in);
            System.out.println("IP:");
            String ip = keyboardScanner.nextLine();
            System.out.println("PORT");
            String port = keyboardScanner.nextLine();

            Socket socket = new Socket(ip, Integer.parseInt(port));
            //Socket socket = new Socket("139.59.215.163", 8080);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            while (socket.isConnected()){
                String msg = keyboardScanner.nextLine();
                System.out.println(msg);
                pw.println(msg);
                System.out.println("Message Send");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
