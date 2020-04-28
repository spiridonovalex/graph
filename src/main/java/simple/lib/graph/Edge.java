package simple.lib.graph;

public class Edge<V, E> {
	private final V from;
	private final V to;
	private final boolean directed;
	private final E payload;
	
	Edge(boolean directed, V from, V to, E payload) {
		this.from = from;
		this.to = to;
		this.directed = directed;
		this.payload = payload;
	}

	public V getFrom() {
		return from;
	}

	public V getTo() {
		return to;
	}

	public E getPayload() {
		return payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (directed ? 1231 : 1237);
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (directed != other.directed)
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String separator = directed ? " => " : " <=> ";
		return "(" + from + separator + to + ", " + payload + ")";
	}
}
