import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private ServerSocket serverSocket;
    private int maxSessionCount;
    private volatile int sessionCount = 0;
    private volatile boolean isAlive;
    private Object lock = new Object();

    public Host(int port, int maxSessions) {
        this.maxSessionCount = maxSessions;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can't start server. Port: " + port);
        }
    }

    public void start() {
        if (isAlive) {
            throw new IllegalStateException("WHY ARE YOU DOING THIS?!");
        }
        this.isAlive = true;
        while (isAlive) {
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                if (sessionCount == maxSessionCount) {
                    dos.writeUTF("Sorry, server too busy. Please try later");
                    socket.close();
                }
                synchronized (lock) {
                    sessionCount++;
                }
                Thread thread1 = new Thread(new Session(socket, this));
                thread1.start();
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
        }
    }
}
