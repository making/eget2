package am.ik.eget2.visited;

public interface Visited<T> {
    void visit(T r);

    boolean isVisited(T r);
}
