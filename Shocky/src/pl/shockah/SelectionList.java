package pl.shockah;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SelectionList<E> implements List<E> {
	protected final List<E> list;
	protected int selected = 0;
	
	public SelectionList(List<E> list) {
		this.list = list;
	}

	public boolean add(E e) {return list.add(e);}
	public void add(int index, E element) {
		if (selected >= index) selected++;
		list.add(index,element);
	}
	public boolean addAll(Collection<? extends E> c) {return list.addAll(c);}
	public boolean addAll(int index, Collection<? extends E> c) {
		int i = 0, size = list.size();
		for (E e : c) add(index+(i++),e);
		return size != list.size();
	}
	public void clear() {
		list.clear();
		selected = 0;
	}
	public boolean contains(Object o) {return list.contains(o);}
	public boolean containsAll(Collection<?> c) {return list.containsAll(c);}
	public E get(int index) {return list.get(index);}
	public int indexOf(Object o) {return list.indexOf(o);}
	public boolean isEmpty() {return list.isEmpty();}
	public Iterator<E> iterator() {return list.iterator();}
	public int lastIndexOf(Object o) {return list.lastIndexOf(o);}
	public ListIterator<E> listIterator() {return list.listIterator();}
	public ListIterator<E> listIterator(int index) {return list.listIterator(index);}
	public boolean remove(Object o) {
		int index = list.indexOf(o);
		if (index == -1) return false;
		remove(index);
		return true;
	}
	public E remove(int index) {
		if (index < selected) {
			selected--;
			if (selected < 0) selected++;
		}
		return list.remove(index);
	}
	public boolean removeAll(Collection<?> c) {
		int size = list.size();
		for (Object o : c) remove(o);
		return size != list.size();
	}
	public boolean retainAll(Collection<?> c) {
		selected = 0;
		return list.retainAll(c);
	}
	public E set(int index, E element) {return list.set(index,element);}
	public int size() {return list.size();}
	public List<E> subList(int fromIndex, int toIndex) {return list.subList(fromIndex,toIndex);}
	public Object[] toArray() {return list.toArray();}
	public <T> T[] toArray(T[] a) {return list.toArray(a);}
	
	public int getSelectedIndex() {
		return selected;
	}
	public E getSelected() {
		return list.get(selected);
	}
}