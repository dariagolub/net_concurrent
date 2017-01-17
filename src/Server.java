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

        Channel<Runnable> channel = new Channel<>(2);
        Host host = new Host(port, channel, 2);
        host.start();
        Dispatcher dispatcher = new Dispatcher(channel);
        dispatcher.start();

    }
}
