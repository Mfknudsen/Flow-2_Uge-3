import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    boolean keepRunning = true;
    //Socket socket = new Socket("139.59.215.163", 8080);
    public Socket socket = null;
    PrintStream pw = null;

    static ClientUpdator updator = null;
    static Client client = null;

    public static void main(String[] args) throws InterruptedException {
        client = new Client();
        updator = new ClientUpdator(client.socket, client);

        Thread t = new Thread(client);
        t.start();

        while (client.keepRunning){
            if(!updator.keepRunning)
                client.keepRunning = false;
        }

        t.join();
    }

    public void run(){
        try {
            Scanner keyboardScanner = new Scanner(System.in);
            System.out.println("IP:");
            String ip = keyboardScanner.nextLine();
            System.out.println("PORT");
            String port = keyboardScanner.nextLine();


            keepRunning = true;
            //Socket socket = new Socket("139.59.215.163", 8080); //Vores Gruppe
            //Socket socket = new Socket("138.68.93.150", 5555);  //Alberts Gruppe.
            //socket = new Socket("localhost", 8080);             //Test
            pw = new PrintStream(socket.getOutputStream(), true);
            updator = new ClientUpdator(socket, this);
            new Thread(updator).start();

            while (keepRunning) {
                String msg = keyboardScanner.nextLine();
                pw.println(msg);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void End(){
        keepRunning = false;
    }


}
