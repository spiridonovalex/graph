package simple.lib.graph;

import java.util.List;
import java.util.function.Consumer;

public interface Graph<V, E> {
	boolean addVertex(V vertex);
	Edge<V,E> addEdge(V from, V to, E payload);
	List<Edge<V,E>> getPath(V from, V to);
	void forEachVertex(Consumer<V> consumer);
}
