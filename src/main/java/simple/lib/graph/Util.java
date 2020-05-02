package simple.lib.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Util {
	private Util() {
	}
	
	public static <V, E> List<Edge<V,E>> concat(List<Edge<V,E>> currentPath, List<Edge<V,E>> addPath) {
		List<Edge<V,E>> list = new ArrayList<>(currentPath.size() + addPath.size());
		list.addAll(currentPath);
		list.addAll(addPath);
		return list;
	}
	
	public static <T> List<T> reverse(List<T> list) {
		Collections.reverse(list);
		return list;
	}
}
