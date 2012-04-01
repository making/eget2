package am.ik.eget2.queue;

public interface TaskQueue<T> {
    T pull();

    void push(T task);
}
