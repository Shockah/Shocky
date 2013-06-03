package pl.shockah;

import java.util.ArrayList;

public class NoDuplicateArrayList<T> extends ArrayList<T> {
	private static final long serialVersionUID = -5601587458508613699L;
	
	public boolean add(T el) {
		if (contains(el)) return false;
		return super.add(el);
	}
}