package simple.lib.graph;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;

public class GraphTest {
	
	@Test(expected = UnknownVertexException.class)
	public void testEmpty() {
		new GraphBuilder<>(true).build().getPath(1, 1);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSingleVertexNull() {
		var graph = new GraphBuilder<Integer, Integer>(false).vertex(null).build();
		assertEquals(null, graph.getPath(1, 1));
	}
	
	@Test
	public void testSingleVertex() {
		var graph = new GraphBuilder<Integer, Integer>(false).vertex(1).build();
		assertEquals(null, graph.getPath(1, 1));
	}
	
	@Test
	public void testSingleVertexCycle() {
		var graph = new GraphBuilder<Integer, Integer>(true).edge(1, 1, 10).build();
		assertEquals(Arrays.asList(new Edge<>(true, 1, 1, 10)), graph.getPath(1, 1));
	}
	
	@Test
	public void testDoubleVertexDirected() {
		var graph = new GraphBuilder<Integer, Integer>(true).edge(1, 2, 10).build();
		assertEquals(null, graph.getPath(1, 1));
		assertEquals(Arrays.asList(new Edge<>(true, 1, 2, 10)), graph.getPath(1, 2));
		assertEquals(null, graph.getPath(2, 1));
	}
	
	@Test
	public void testDoubleVertexNonDirected() {
		var graph = new GraphBuilder<Integer, Integer>(false).edge(1, 2, 10).build();
		assertEquals(null, graph.getPath(1, 1));
		assertEquals(Arrays.asList(new Edge<>(false, 1, 2, 10)), graph.getPath(1, 2));
		assertEquals(Arrays.asList(new Edge<>(false, 1, 2, 10)), graph.getPath(2, 1));
	}
	
	@Test
	public void testNonDirected() {
		var graph = new GraphBuilder<Integer, Integer>(false)
				.edge(1, 2, 10)
				.edge(2, 3, 10)
				.edge(3, 4, 10)
				.edge(4, 5, 10)
				.vertex(6)
				.build();
		
		assertEquals(Arrays.asList(
				new Edge<>(false, 1, 2, 10),
				new Edge<>(false, 2, 3, 10),
				new Edge<>(false, 3, 4, 10),
				new Edge<>(false, 4, 5, 10)),
				graph.getPath(1, 5));
		
		assertEquals(Arrays.asList(
				new Edge<>(false, 4, 5, 10),
				new Edge<>(false, 3, 4, 10),
				new Edge<>(false, 2, 3, 10),
				new Edge<>(false, 1, 2, 10)),
				graph.getPath(5, 1));

		
		assertEquals(null,
				graph.getPath(1, 6));
	}
	
	@Test
	public void testDirected() {
		var graph = new GraphBuilder<Integer, Integer>(true)
				.edge(1, 2, 10)
				.edge(2, 3, 10)
				.edge(1, 3, 10)
				.edge(3, 4, 10)
				.edge(4, 5, 10)
				.vertex(6)
				.build();
		
		assertEquals(Arrays.asList(
				new Edge<>(true, 1, 3, 10),
				new Edge<>(true, 3, 4, 10),
				new Edge<>(true, 4, 5, 10)),
				graph.getPath(1, 5));
		
		assertEquals(null,
				graph.getPath(5, 1));
		
		assertEquals(null,
				graph.getPath(1, 6));
	}
}
