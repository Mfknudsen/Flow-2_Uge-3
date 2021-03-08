import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            //Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            /*TEMP*/Socket socket = new Socket("139.59.215.163", 8080);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            Scanner ss = new Scanner(socket.getInputStream());

            Scanner keyboard = new Scanner(System.in);
            pw.println(keyboard.nextLine());

            String[] result = ss.nextLine().split("#");

            if(result[0].equals("y")){
                pw.println("You Are Connected As " + result[1] + " With ID " + result[2]);
            }
            else if(result[0].equals("n")){
                pw.println("You Weren't Connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
