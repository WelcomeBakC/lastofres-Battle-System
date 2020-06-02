package util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CompositeFunction<T, R> implements Function<T, R> {
	
	public class Head<U, S> implements Comparable<Head<U, S>>{
		
		private final Function<U, S> function;
		private final int priority;
		
		public Head(Function<U, S> function, int priority) {
			this.function = function;
			this.priority = Math.floorMod(priority, Integer.MAX_VALUE);
		}
		
		public Function<U, S> getFunction() {
			return function;
		}
		
		@Override
		public int compareTo(Head<U, S> o) {
			return priority - o.priority;
		}
	}
	
	
	private PriorityQueue<Head<T, R>> headQueue = new PriorityQueue<Head<T, R>>();
	private ArrayList<Function<R, R>> tailList = new ArrayList<Function<R, R>>();
	
	private boolean updated;
	
	private Function<T, R> composedFunction;
	
	
	// constructor for non-empty headQueue
	public CompositeFunction(Function<T, R> head, int priority) {
		addHead(head, priority);
	}
	
	
	public List<Function<T, R>> getHeadQueueAsList() {
		return headQueue.stream().map(head -> head.getFunction()).collect(Collectors.toList());
	}
	
	public Function<T, R> getComposedFunction() {
		if (updated) {
			updated = false;
			return composedFunction = composeAll();
		} else {
			return composedFunction;
		}
	}
	
	public void addHead(Function<T, R> head, int priority) {
		headQueue.add(new Head<T, R>(head, priority));
		updated = true;
	}
	
	public boolean removeHead(Function<T, R> head) {
		return headQueue.removeIf(f -> f.getFunction().equals(head)) ? updated = true : false;
	}
	
	public void addTailSegment(Function<R, R> segment) {
		tailList.add(segment);
		updated = true;
	}
	
	public boolean removeTailSegment(Function<R, R> segment) {
		return tailList.remove(segment) ? updated = true : false;
	}
	
	private Function<T, R> composeAll() {
		Function<T, R> head = headQueue.peek().getFunction();
		if (tailList.isEmpty()) {
			return head;
		} else {
			Function<R, R> tail = tailList.stream().reduce((f, g) -> f.andThen(g)).get();
			
			return head.andThen(tail);
		}
	}

	@Override
	public R apply(T t) {
		return getComposedFunction().apply(t);
	}
	
}