package utils;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> extends AbstractCollection<E>{
	private static class Entry<E> {
		private E element;
		private Entry<E> next;
		private Entry<E> prev;

		Entry(E element, Entry<E> next, Entry<E> prev) {
			this.element = element;
			this.next = next;
			this.prev = prev;
		}
	}

	private class MyLinkedListIterator implements Iterator<E> {
		private Entry<E> current;
		private boolean isRemoved;
		
		private MyLinkedListIterator() {
			this.current = header;
			this.isRemoved = false;
		}

		@Override
		public E next() {
			current = current.next;
			if (current == header)
				throw new NoSuchElementException();
			isRemoved = false;
			return current.element;
		}

		@Override
		public void remove() {		
			if (current == header || isRemoved)
				throw new IllegalStateException();
			isRemoved = true;
			removeEntry(current);
		}

		@Override
		public boolean hasNext() {
			if (current.next == header)
				return false;
			else
				return true;
		}
	}

	private int size;
	private Entry<E> header;

	public MyLinkedList() {
		size = 0;
		header = new Entry<E>(null, null, null);
		header.prev = header.next = header;
	}

	@Override
	public boolean add(E e) {
		insert(e, header);
		return true;
	}

	public boolean add(E e, int index) {
		insert(e, getEntry(index));
		return true;
	}

	// insert newEntry before current
	private void insert(E e, Entry<E> current) {
		Entry<E> newEntry = new Entry<E>(e, current, current.prev);
		newEntry.prev.next = newEntry;
		newEntry.next.prev = newEntry;
		size++;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c)
			add(e);
		return true;
	}

	@Override
	public void clear() {
		header.prev = header.next = header;
		size = 0;
	}

	@Override
	public boolean contains(Object o) {
		return getEntry(o) != null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new MyLinkedListIterator();
	}

	@Override
	public boolean remove(Object o) {
		Entry<E> entry = getEntry(o);	
		return entry != null ? removeEntry(entry) : false;
	}

	public boolean remove(int index) {
		Entry<E> e = getEntry(index);	
		return removeEntry(e);
	}

	private boolean removeEntry(Entry<E> e) {
		if (e == null)
			throw new NoSuchElementException();
		// change pointers
		e.prev.next = e.next;
		e.next.prev = e.prev;
		size--;
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean isModified = false;
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			if (c.contains(it.next())) {
				it.remove();
				isModified = true;
			}
		}
		return isModified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean isModified = false;
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			if (!c.contains(it.next())) {
				it.remove();
				isModified = true;
			}
		}
		return isModified;
	}

	@Override
	public int size() {
		return size;
	}

	private Entry<E> getEntry(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);

		Entry<E> e = header;

		if (index < (size >> 1))
			for (int i = 0; i <= index; i++) {
				e = e.next;
			}
		else
			for (int i = size; i > index; i--) {
				e = e.prev;
			}

		return e;
	}
	
	private Entry<E> getEntry(Object o){
		Entry<E> entry = header;
		while (entry.next != null) {
			entry = entry.next;
			if (o == null ? entry.element == o : o.equals(entry.element))
					return entry;
		}
		return null;	
	}
	
	public E getElement(int index){
		return getEntry(index).element;		
	}
}
