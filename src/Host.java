import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private ServerSocket serverSocket;
    private int maxSessionCount;
    private int sessionCount = 0;

    public Host(int port, int maxSessions) {
        this.maxSessionCount = maxSessions;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can't start server. Port: " + port);
        }
    }

    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                if (sessionCount == maxSessionCount) {
                    dos.writeUTF("Sorry, server too busy. Please try later");
                    socket.close();
                }
                sessionCount++;
                Thread thread1 = new Thread(new Session(socket, this));
                thread1.start();
            } catch (IOException e) {
                System.out.println("Connection interrupted");
            }
        }
    }

    public void closeSession() {
        sessionCount--;
    }
}
