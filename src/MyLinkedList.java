import java.util.Collection;
import java.util.Iterator;

public class MyLinkedList<E> implements java.util.Collection<E> {
	static class Entry<E> {
		E element;
		Entry<E> next;
		Entry<E> prev;

		Entry(E element, Entry<E> next, Entry<E> prev) {
			this.element = element;
			this.next = next;
			this.prev = prev;
		}
	}

	private class MyLinkedListIterator implements Iterator<E> {
		private Entry<E> current;
		private int currentIndex = -1;

		private MyLinkedListIterator() {
			this.current = MyLinkedList.this.header;
		}

		@Override
		public E next() {
			current = current.next;
			currentIndex += 1;
			return current.element;
		}

		@Override
		public void remove() {
			MyLinkedList.this.remove(current);
		}

		@Override
		public boolean hasNext() {
			if (currentIndex < MyLinkedList.this.size)
				return true;
			else
				return false;
		}
	}

	private int size;
	 Entry<E> header;

	public MyLinkedList() {
		size = 0;
		header = new Entry<E>(null, header, header);
		// TODO find out why constructor does't work in this case
		header.prev = header.next = header;
	}

	@Override
	public boolean add(E e) {
		insert(e, header);
		return true;
	}

	public boolean add(E e, int index) {
		insert(e, getElement(index));
		return true;
	}

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
		Iterator<E> it = iterator();
		while (it.hasNext()) {
			if (it.next().equals(o))
				return true;
		}
		return false;
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

	// TODO ask about this
	@Override
	public boolean remove(Object o) {
		Entry<E> e = header;
		int i = 0;
		while (i < size) {
			e = e.next;
			if (e.element.equals(o)) {
				removeEntry(e);
				return true;
			}
			i++;
		}
		return false;
	}

	public boolean remove(int index) {
		Entry<E> e = getElement(index);
		removeEntry(e);
		return true;
	}

	private boolean removeEntry(Entry<E> e) {
		// change pointers
		e.prev.next = e.next;
		e.next.prev = e.prev;
		// delete entry
		e.next = e.prev = null;
		e.element = null;
		size--;
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean flag = false;
		for (Object o : c)
			if (remove(o))
				flag = true;
		return flag;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean flag = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                flag = true;
            }
        }
        return flag;
		
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		Iterator<E> it = iterator();
		int i = 0;
		while (it.hasNext()) {
			array[i++] = it.next();
		}
		return array;
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	private Entry<E> getElement(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);

		Entry<E> e = header;

		if (index < (size >> 1)) {
			for (int i = 0; i <= index; i++)
				e = e.next;
		} else {
			for (int i = size; i > index; i--)
				e = e.prev;
		}

		return e;
	}
}
