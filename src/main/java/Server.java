import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final  int DEFAULT_PORT = 2345;
    private ServerSocket serverSocket = null;
    private static final List<ClientHandler> handlers = new ArrayList<>();
}
