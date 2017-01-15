import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connection established with: " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
            outputStream.writeUTF("Connection open");
            String message = inputStream.readUTF();
            while (!"exit".equalsIgnoreCase(message)) {
                System.out.println("Client send me this message: " + message);
                outputStream.writeUTF("Got your message: " + message);
                message = inputStream.readUTF();
            }
            outputStream.writeUTF("Connection closed");
            System.out.println("Connection closed with : " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
        } catch (IOException e) {
            System.out.println("Connection with " + socket.getPort() + " is interrupted");
        }
    }
}