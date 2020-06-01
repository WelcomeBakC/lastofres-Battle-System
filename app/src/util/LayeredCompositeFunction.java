package util;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class LayeredCompositeFunction<T, R> implements Function<T, R> {
	
	private boolean updated;
	
	private Function<T, R> composedFunction;
	
	private ArrayList<CompositeFunction> layers;
	private ArrayList<Class> layerTypes;
	
	public void foo(Function f) {
		if (layerTypes.contains(f.getClass())) {
			// something
		}
	}
	
	protected void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	public Function<T, R> getComposedFunction() {
		if (updated) {
			updated = false;
			return composedFunction = composeAll();
		} else {
			return composedFunction;
		}
	}
	
	public abstract <U, S> void addHead(Function<U, S> function, int priority, int layer);
	
	public abstract Function<T, R> composeAll();

	@Override
	public R apply(T t) {
		return getComposedFunction().apply(t);
	}
	
}
