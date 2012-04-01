package am.ik.eget2.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;

import am.ik.eget2.visited.Visited;

public class HazelcastVisited<T> extends AbstractHazelcastCollection implements
        Visited<T> {
    private final ISet<T> visited;

    public HazelcastVisited(String visitedName,
            HazelcastInstance hazelcastInstance) {
        this.visited = hazelcastInstance.getSet(visitedName);
    }

    public void visit(T r) {
        visited.add(r);
    }

    public boolean isVisited(T r) {
        return visited.contains(r);
    }

    @Override
    protected void clearCollection() {
        visited.clear();
    }
}
