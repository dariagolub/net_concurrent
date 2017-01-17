public class WorkerThread implements Task {
    private final Thread thread;
    private final ThreadPool threadPool;
    private final Object lock = new Object();
    private volatile boolean isActive;
    private Task currentTask;

    public WorkerThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
        this.thread = new Thread(this);
        this.isActive = true;
        thread.start();
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (isActive) {
                while (currentTask == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        if (!isActive) {
                            return;
                        }
                    }
                }
                try {
                    if (!isActive) {
                        return;
                    }
                    currentTask.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    currentTask = null;
                    threadPool.onTaskCompleted(this);
                }
            }
        }
    }

    public void execute(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must be not null");
        }
        synchronized (lock) {
            if (currentTask != null) {
                throw new IllegalStateException("Previous task is still executing");
            }
            currentTask = task;
            lock.notify();
        }
    }
}
