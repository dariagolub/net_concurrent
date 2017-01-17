public class Dispatcher implements Runnable {
    private final Channel<Task> channel;
    private volatile boolean isActive;
    private Thread thread;
    private final ThreadPool threadPool;

    public Dispatcher(Channel<Task> channel, ThreadPool threadPool) {
        this.channel = channel;
        this.threadPool = threadPool;
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
                threadPool.execute(channel.get());
            } catch (InterruptedException e) {
                if (!isActive) {
                    return;
                }
            }
        }
    }

}
