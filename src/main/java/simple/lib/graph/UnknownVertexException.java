package simple.lib.graph;

import java.util.Objects;

public class UnknownVertexException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnknownVertexException(Object vertex) {
		super("Unknown vertex " + Objects.toString(vertex));
	}
}
