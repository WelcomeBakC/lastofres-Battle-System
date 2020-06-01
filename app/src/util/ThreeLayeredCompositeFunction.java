package util;

import java.util.function.Function;

public class ThreeLayeredCompositeFunction<T, U, V, R> extends LayeredCompositeFunction<T, R> {

	private CompositeFunction<T, U> layer1;
	private CompositeFunction<U, V> layer2;
	private CompositeFunction<V, R> layer3;
	

	public void addHead1(Function<T, U> head, int priority) {
		layer1.addHead(head, priority);
		updated = true;
	}
	
	public void addHead2(Function<U, V> head, int priority) {
		layer2.addHead(head, priority);
		updated = true;
	}

	public void addHead3(Function<V, R> head, int priority) {
		layer3.addHead(head, priority);
		updated = true;
	}
	
	public boolean removeHead1(Function<T, U> head) {
		return layer1.removeHead(head) ? updated = true : false;
	}
	
	public boolean removeHead2(Function<U, V>head) {
		return layer2.removeHead(head) ? updated = true : false;
	}
	
	public boolean removeHead3(Function<V, R> head) {
		return layer3.removeHead(head) ? updated = true : false;
	}

	public void addTailSegment1(Function<U, U> segment) {
		layer1.addTailSegment(segment);
		updated = true;
	}
	
	public void addTailSegment2(Function<V, V> segment) {
		layer2.addTailSegment(segment);
		updated = true;
	}

	public void addTailSegment3(Function<R, R> segment) {
		layer3.addTailSegment(segment);
		updated = true;
	}
	
	public boolean removeTailSegment1(Function<U, U> segment) {
		return layer1.removeTailSegment(segment) ? updated = true : false;
	}
	
	public boolean removeTailSegment2(Function<V, V> segment) {
		return layer2.removeTailSegment(segment) ? updated = true : false;
	}
	
	public boolean removeTailSegment3(Function<R, R> segment) {
		return layer3.removeTailSegment(segment) ? updated = true : false;
	}

	@Override
	public Function<T, R> composeAll() {
		Function<T, U> function1 = layer1.getComposedFunction();
		Function<U, V> function2 = layer2.getComposedFunction();
		Function<V, R> function3 = layer3.getComposedFunction();
		return function1.andThen(function2).andThen(function3);
	}
}
