package simple.lib.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<V, E> {	
	private final Map<V, Edge<V,E>> emptyEdges = Collections.emptyMap();
	
	private final Map<V, Map<V, Edge<V,E>>> vertexes = new HashMap<>();
	private final boolean directed;
	
	public static <V, E> GraphBuilder<V, E> builder(boolean directed) {
		return new GraphBuilder<V, E>(directed);
	}
	
	public Graph(boolean directed) {
		this.directed = directed;
	}
	
	public boolean addVertex(V vertex) {
		if (vertex == null) {
			throw new NullPointerException();
		}
		
		return vertexes.putIfAbsent(vertex, emptyEdges) == null;
	}
	
	public Edge<V,E> addEdge(V from, V to, E payload) {
		if (from == null || to == null) {
			throw new NullPointerException();
		}
		
		addVertex(from);
		addVertex(to);
		
		Edge<V,E> e = new Edge<>(directed, from, to, payload);
		addEdgeToMap(from, to, e);
		if (!directed) {
			addEdgeToMap(to, from, e);
		}
		return e;
	}
	
	private void addEdgeToMap(V from, V to, Edge<V,E> edge) {
		vertexes.compute(from, this::addVertexMap).put(to, edge);
	}
	
	private Map<V, Edge<V,E>> addVertexMap(V from, Map<V, Edge<V,E>> current) {
		return current == null || current == emptyEdges ? new HashMap<>() : current;
	}
	
	public List<Edge<V,E>> getPath(V from, V to) {
		if (from == null || to == null) {
			throw new NullPointerException();
		}
		if (!vertexes.containsKey(from)) {
			throw new UnknownVertexException(from);
		}
		if (!vertexes.containsKey(to)) {
			throw new UnknownVertexException(to);
		}
		
		if (from.equals(to)) {
			Edge<V, E> edge = this.vertexes.get(from).get(to);
			return edge == null ? null : Collections.singletonList(edge);
		}
		
		GraphTraces fromTraces = new GraphTraces(false, from, Collections.emptyList());
		GraphTraces toTraces = new GraphTraces(true, to, Collections.emptyList());
		
		while (true) {
			GraphTraces toExtend;
			GraphTraces toCheck;
			if (directed || fromTraces.size() < toTraces.size()) {
				toExtend = fromTraces;
				toCheck = toTraces;
			} else {
				//In case of non-directed large graph it is much faster to extend traces from both ends
				toExtend = toTraces;
				toCheck = fromTraces;
			}
			
			if (!toExtend.extend()) {
				return null;
			}
			
			List<Edge<V,E>> path = toExtend.intersects(toCheck);
			if (path != null) {
				return path;
			}
		}
	}
	
	private class GraphTraces {
		private final Map<V, List<Edge<V,E>>> map;
		private final boolean inverted;
		
		public GraphTraces(boolean inverted, V initialVertex, List<Edge<V,E>> initialTrace) {
			this.map = new HashMap<>(Collections.singletonMap(initialVertex, initialTrace));
			this.inverted = inverted;
		}
		
		public int size() {
			return map.size();
		}
		
		public List<Edge<V,E>> intersects(GraphTraces traces) {
			if (size() > traces.size()) {
				return traces.intersects(this);
			}
			
			for (Map.Entry<V, List<Edge<V,E>>> e : map.entrySet()) {
				List<Edge<V,E>> trace = traces.map.get(e.getKey());
				if (trace != null) {
					return inverted ? 
							Util.concat(trace, Util.reverse(e.getValue())) : 
							Util.concat(e.getValue(), Util.reverse(trace));
				}
			}
			
			return null;
		}
		
		public boolean extend() {
			Map<V, List<Edge<V,E>>> extension = new HashMap<>();
			map.forEach((v, path) -> extend(v, path, extension));
			//Exclude already existing vertexes because their traces are shorter
			extension.keySet().removeAll(map.keySet());
			if (extension.isEmpty()) {
				//Unable to get any progress. Looks like path does not exist
				return false;
			}
			map.putAll(extension);
			return true;
		}
		
		private void extend(V from, List<Edge<V,E>> currentPath, Map<V, List<Edge<V,E>>> extension) {
			vertexes.get(from).forEach((to, edge) -> extension.put(to, Util.concat(currentPath, edge)));
		}
	}
}
