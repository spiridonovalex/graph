package simple.lib.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<V, E> {	
	private final Map<V, Edge<V,E>> emptyEdges = Collections.emptyMap();
	
	private final Map<V, Map<V, Edge<V,E>>> vertices = new HashMap<>();
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
		
		return vertices.putIfAbsent(vertex, emptyEdges) == null;
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
		vertices.compute(from, this::addVertexMap).put(to, edge);
	}
	
	private Map<V, Edge<V,E>> addVertexMap(V from, Map<V, Edge<V,E>> current) {
		return current == null || current == emptyEdges ? new HashMap<>() : current;
	}
	
	public List<Edge<V,E>> getPath(V from, V to) {
		if (from == null || to == null) {
			throw new NullPointerException();
		}
		if (!vertices.containsKey(from)) {
			throw new UnknownVertexException(from);
		}
		if (!vertices.containsKey(to)) {
			throw new UnknownVertexException(to);
		}
		
		if (from.equals(to)) {
			Edge<V, E> edge = this.vertices.get(from).get(to);
			return edge == null ? null : Collections.singletonList(edge);
		}
		
		GraphTraces fromTraces = new GraphTraces(false, from);
		GraphTraces toTraces = new GraphTraces(true, to);
		
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
		private final Map<V, V> map;
		private final boolean inverted;
		
		public GraphTraces(boolean inverted, V initialVertex) {
			this.map = new HashMap<>(Collections.singletonMap(initialVertex, null));
			this.inverted = inverted;
		}
		
		public int size() {
			return map.size();
		}
		
		public List<Edge<V,E>> intersects(GraphTraces traces) {
			if (size() > traces.size()) {
				return traces.intersects(this);
			}
			
			for (V key : map.keySet()) {
				if (traces.map.containsKey(key)) {
					return inverted ? 
							Util.concat(Util.reverse(traces.toTrace(key)), toTrace(key)) : 
							Util.concat(Util.reverse(toTrace(key)), traces.toTrace(key));
				}
			}
			
			return null;
		}
		
		public List<Edge<V,E>> toTrace(V start) {
			List<Edge<V,E>> result = new ArrayList<>();
			V current = start;
			V prev = map.get(current);
			while (prev != null) {
				result.add(vertices.get(prev).get(current));
				current = prev;
				prev = map.get(current);
			}
			return result;
		}
		
		public boolean extend() {
			Map<V, V> extension = new HashMap<>();
			map.keySet().forEach(v -> extend(v, extension));
			//Exclude already existing vertices because their traces are shorter
			extension.keySet().removeAll(map.keySet());
			if (extension.isEmpty()) {
				//Unable to get any progress. Looks like path does not exist
				return false;
			}
			map.putAll(extension);
			return true;
		}
		
		private void extend(V from, Map<V, V> extension) {
			vertices.get(from).keySet().forEach(to -> extension.put(to, from));
		}
	}
}
