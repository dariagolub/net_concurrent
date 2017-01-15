import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: address port");
            return;
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + args[0]);
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Wrong port format. Should be integer");
            return;
        }
        Socket socket = null;
        try {
            socket = new Socket(inetAddress, port);
            System.out.println("Local port: " + socket.getLocalPort());

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            System.out.println(dis.readUTF());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                line = bufferedReader.readLine();
                dos.writeUTF(line);
                System.out.println(dis.readUTF());
            } while (!"exit".equalsIgnoreCase(line));

        } catch (IOException | NullPointerException e) {
            System.out.printf("Connection with server interrupted");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
