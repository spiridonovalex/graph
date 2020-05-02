package simple.lib.graph;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class ReadWriteGraph<V, E> implements Graph<V, E> {
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Graph<V, E> wrapped;
	
	public ReadWriteGraph(Graph<V, E> graph) {
		this.wrapped = graph;
	}
	
	@Override
	public boolean addVertex(V vertex) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			return wrapped.addVertex(vertex);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public Edge<V, E> addEdge(V from, V to, E payload) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			return wrapped.addEdge(from, to, payload);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public List<Edge<V, E>> getPath(V from, V to) {
		Lock readLock = lock.readLock();
		readLock.lock();
		try {
			return wrapped.getPath(from, to);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public void forEachVertex(Consumer<V> consumer) {
		Lock readLock = lock.readLock();
		readLock.lock();
		try {
			wrapped.forEachVertex(consumer);
		} finally {
			readLock.unlock();
		}
	}

}
