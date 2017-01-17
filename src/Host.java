import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    private ServerSocket serverSocket;
    private volatile boolean isAlive;
    private final Channel<Task> channel;
    private Thread thread;

    public Host(int port, Channel<Task> channel) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can't start server. Port: " + port);
        }
        this.channel = channel;
    }

    public void start() {
        thread = new Thread(this);
        if (isAlive)
            throw new IllegalStateException("Host already started");
        this.isAlive = true;
        thread.start();
        System.out.println("Host has started");
    }

    @Override
    public void run() {
        while (isAlive) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                channel.put(new Session(socket));
            } catch (IOException e) {
                close();
            }
        }
    }

    private void close() {
        if (isAlive) {
            isAlive = false;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Host is down");
        }
    }

}
