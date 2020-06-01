package util;

import java.util.function.Function;

public class TwoLayeredCompositeFunction<T, U, R> extends LayeredCompositeFunction<T, R> {
	
	private CompositeFunction<T, U> layer0;
	private CompositeFunction<U, R> layer1;


	@Override
	public <U, S> void addHead(Function<U, S> function, int priority, int layer) {
		switch (layer) {
		case 0:
			layer0.addHead(function, priority);
		}
	}

	public void addHead0(Function<T, U> head, int priority) {
		layer0.addHead(head, priority);
		updated = true;
	}
	
	public void addHead1(Function<U, R> head, int priority) {
		head.
		layer1.addHead(head, priority);
		updated = true;
	}
	
	public boolean removeHead0(Function<T, U> head) {
		return layer0.removeHead(head) ? updated = true : false;
	}
	
	public boolean removeHead1(Function<U, R> head) {
		return layer1.removeHead(head) ? updated = true : false;
	}

	public void addTailSegment0(Function<U, U> segment) {
		layer0.addTailSegment(segment);
		updated = true;
	}
	
	public void addTailSegment1(Function<R, R> segment) {
		layer1.addTailSegment(segment);
		updated = true;
	}
	
	public boolean removeTailSegment0(Function<U, U> segment) {
		return layer0.removeTailSegment(segment) ? updated = true : false;
	}
	
	public boolean removeTailSegment1(Function<R, R> segment) {
		return layer1.removeTailSegment(segment) ? updated = true : false;
	}

	@Override
	public Function<T, R> composeAll() {
		Function<T, U> function0 = layer0.getComposedFunction();
		Function<U, R> function1 = layer1.getComposedFunction();
		return function0.andThen(function1);
	}

}
