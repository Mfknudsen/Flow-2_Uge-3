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
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintStream pw = new PrintStream(socket.getOutputStream(), true);

            DataInputStream data = new DataInputStream(socket.getInputStream());

            while (keepRunning){
                String msg = keyboardScanner.nextLine();
                pw.println(msg);
                System.out.println("Message Sent");
                String s = scanner.nextLine();
                System.out.println(s);
                System.out.println("Message Received");

                keepRunning = !s.equals("");
            }

                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
