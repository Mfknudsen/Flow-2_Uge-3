import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner keyboardScanner = new Scanner(System.in);
            /* For All Purpose Use
            System.out.println("IP:");
            //String ip = keyboardScanner.nextLine();
            //System.out.println("PORT");
            //String port = keyboardScanner.nextLine();
            //Socket socket = new Socket(ip, Integer.parseInt(port));
            */

            boolean keepRunning = true;
            Socket socket = new Socket("localhost", 8080);
            PrintStream pw = new PrintStream(socket.getOutputStream(), true);

            ClientUpdator updator = new ClientUpdator(socket);
            new Thread(updator).start();

            while (keepRunning) {
                String msg = keyboardScanner.nextLine();
                pw.println(msg);
            }

            socket.close();
            updator.End();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
