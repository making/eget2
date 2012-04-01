package am.ik.eget2.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleTaskQueue<T> implements TaskQueue<T> {
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();

    @Override
    public T pull() {
        return queue.poll();
    }

    public void push(T task) {
        queue.add(task);
    };
}
