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

            Socket socket = new Socket("localhost", 8080);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintStream pw = new PrintStream(socket.getOutputStream(), true);

            DataInputStream data = new DataInputStream(socket.getInputStream());

            while (socket.isConnected()){
                String msg = keyboardScanner.nextLine();
                pw.println(msg);

                System.out.println((String) data.readUTF());
                System.out.println("end");
                //String s = scanner.nextLine();
                //System.out.println(s);
                //System.out.println("Message Received");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
