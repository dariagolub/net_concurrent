import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    private ServerSocket serverSocket;
    private int maxSessionCount;
    private volatile int sessionCount = 0;
    private volatile boolean isAlive;
    private final Channel<Runnable> channel;
    private Thread thread;
    private final Object lock = new Object();

    public Host(int port, Channel<Runnable> channel, int maxSessions) {
        this.maxSessionCount = maxSessions;
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
                synchronized (lock) {
                    socket = serverSocket.accept();
                    while (sessionCount == maxSessionCount) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    sessionCount++;
                }
                channel.put(new Session(socket, this));
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

    public void closeSession() {
        synchronized (lock) {
            sessionCount--;
            lock.notifyAll();
        }
    }
}
