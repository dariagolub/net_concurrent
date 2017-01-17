import java.util.LinkedList;

public class ThreadPool {
    private final LinkedList<WorkerThread> allWorkers = new LinkedList<>();
    private final Channel<Task> freeWorkers;
    private final Object lock = new Object();
    private final int maxSize;
    private int currentSize;

    public ThreadPool(int maxSize) {
        WorkerThread worker;
        this.maxSize = maxSize;
        this.currentSize = 1;
        freeWorkers = new Channel<>(maxSize);
        worker = new WorkerThread(this);
        allWorkers.addLast(worker);
        freeWorkers.put(worker);
    }

    public void execute(Task task) throws InterruptedException {
        if (task == null) {
            throw new IllegalArgumentException("Task must be not null");
        }
        if (freeWorkers.size() <= 0) {
            synchronized (lock) {
                if (maxSize > allWorkers.size()) {
                    WorkerThread worker = new WorkerThread(this);
                    allWorkers.addLast(worker);
                    freeWorkers.put(worker);
                    currentSize++;
                }
            }
        }
        ((WorkerThread) freeWorkers.get()).execute(task);

    }

    void onTaskCompleted(WorkerThread workerThread) {
        freeWorkers.put(workerThread);
    }

}
