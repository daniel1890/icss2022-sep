package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListIterator<T> implements Iterator<T> {
    private ListNode<T> current;

    public ListIterator(ListNode<T> first) {
        current = first;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        ListNode<T> temp = current;
        current = current.getNext();
        return temp.getData();
    }
}