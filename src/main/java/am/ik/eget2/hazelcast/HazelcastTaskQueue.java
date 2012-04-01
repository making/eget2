package am.ik.eget2.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import am.ik.eget2.queue.TaskQueue;

public class HazelcastTaskQueue<T> extends AbstractHazelcastCollection implements TaskQueue<T> {
    final private IQueue<T> queue;

    public HazelcastTaskQueue(String queueName,
            HazelcastInstance hazelcastInstance) {
        this.queue = hazelcastInstance.getQueue(queueName);
    }

    @Override
    public T pull() {
        return queue.poll();
    }

    @Override
    public void push(T task) {
        queue.add(task);
    }

    @Override
    protected void clearCollection() {
        queue.clear();
    }
}
