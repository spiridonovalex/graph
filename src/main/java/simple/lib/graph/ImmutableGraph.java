package simple.lib.graph;

import java.util.List;
import java.util.function.Consumer;

public class ImmutableGraph<V, E> implements Graph<V, E> {

	private final Graph<V, E> wrapped;
	
	public ImmutableGraph(Graph<V, E> graph) {
		this.wrapped = graph;
	}
	
	@Override
	public boolean addVertex(V vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge<V, E> addEdge(V from, V to, E payload) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Edge<V, E>> getPath(V from, V to) {
		return wrapped.getPath(from, to);
	}

	@Override
	public void forEachVertex(Consumer<V> consumer) {
		wrapped.forEachVertex(consumer);
	}
}
