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

        Channel<Task> channel = new Channel<>(2);
        Host host = new Host(port, channel);
        host.start();
        ThreadPool threadPool = new ThreadPool(2);
        Dispatcher dispatcher = new Dispatcher(channel, threadPool);
        dispatcher.start();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Start shutdown sequence");
            host.stop();
            dispatcher.stop();
            threadPool.stop();
        }));

    }
}
