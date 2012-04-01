package am.ik.eget2.visited;

import com.skjegstad.utils.BloomFilter;

public class SimpleVisited<T> implements Visited<T> {
    private final BloomFilter<T> bloomFilter = new BloomFilter<T>(0.1, 10000);

    @Override
    public void visit(T r) {
        bloomFilter.add(r);
    }

    @Override
    public boolean isVisited(T r) {
        return bloomFilter.contains(r);
    }

}
