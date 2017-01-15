import java.io.*;
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

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connection established with: " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
            outputStream.writeUTF("Connection open");

            while (true) {
                String message = inputStream.readUTF();
                System.out.println("Client send me this message: " + message);
                outputStream.writeUTF("Got your message: " + message);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
