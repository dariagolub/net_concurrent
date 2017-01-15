import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: port");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            System.out.println("Wrong port format. Should be integer");
            return;
        }

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Thread thread1 = new Thread(new Session(socket));
                thread1.start();
            } catch (IOException e) {
                System.out.println("Connection interrupted");
            }
        }

    }
}
