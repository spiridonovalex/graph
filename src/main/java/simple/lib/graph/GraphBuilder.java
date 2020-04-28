package simple.lib.graph;

public class GraphBuilder<V, E> {
	private final Graph<V, E> graph;
	
	public GraphBuilder(boolean directed) {
		this.graph = new Graph<>(directed);
	}
	
	public GraphBuilder<V, E> edge(V from, V to, E payload) {
		this.graph.addEdge(from, to, payload);
		return this;
	}
	
	public GraphBuilder<V, E> vertex(V vertex) {
		this.graph.addVertex(vertex);
		return this;
	}
	
	public Graph<V, E> build() {
		return graph;
	}
}
