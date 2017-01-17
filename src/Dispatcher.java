public class Dispatcher implements Runnable {
    private final Channel<Runnable> channel;
    private volatile boolean isActive;
    private Thread thread;

    public Dispatcher(Channel<Runnable> channel) {
        this.channel = channel;
    }

    public void start() {
        thread = new Thread(this);
        isActive = true;
        thread.start();
        System.out.println("Dispatcher has started");
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                Runnable task = channel.get();
                Thread thread = new Thread(task);
                thread.start();

            } catch (InterruptedException e) {
                if (!isActive) {
                    return;
                }
            }
        }
    }

}
