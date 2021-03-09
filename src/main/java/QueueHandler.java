import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueHandler implements Runnable{
    Server serverReference = null;
    BlockingDeque queue = new LinkedBlockingDeque(100);

    public QueueHandler(Server serverReference) {
        this.serverReference = serverReference;
    }

    @Override
    public void run() {
        while(true){
            try {
                String command = (String) queue.take();

                if(!command.equals("")){
                    serverReference.ServerCommands(command);
                    Thread.sleep(10);
                }
            } catch (Exception e){

            }
        }
    }

    public void PutString(String command) {
        try {
            queue.put(command);
        } catch (Exception e){
        }
    }
}
