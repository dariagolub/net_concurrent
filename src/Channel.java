import java.util.LinkedList;

public class Channel<T> {
    private final LinkedList<Object> queue = new LinkedList<>();
    private final int maxObjects;
    private final Object lock = new Object();

    public Channel(int maxObjects) {
        this.maxObjects = maxObjects;
    }

    public void put(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Element must be not null");
        }
        synchronized (lock) {
            while (queue.size() == maxObjects) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(obj);
            lock.notifyAll();
        }
    }

    public T get() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait();
            }
            lock.notifyAll();
            return (T) queue.removeFirst();
        }
    }

}
