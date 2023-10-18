package nl.han.ica.datastructures;

import java.util.Iterator;

public class HANLinkedList<T> implements  IHANLinkedList<T>{
    private ListNode<T> head;
    private int length;
    @Override
    public void addFirst(T value) {
        if (this.head == null) {
            this.head = new ListNode<>(element);
        }
        else {
            ListNode<T> newNode = new ListNode<>(element);
            newNode.setNext(this.head);
            this.head = newNode;
        }

        this.length++;
    }

    @Override
    public void clear() {
        this.head = null;
    }

    @Override
    public void insert(int index, T value) {
        ListNode<T> temp = new ListNode<>(value);
        ListNode<T> current = head;

        if (head == null) {
            head = temp;
            return;
        }
        if (index == 0) {
            temp.setNext(current);
            head = temp;
            return;
        }
        if (index > getSize()) {
            return;
        }

        int count = 0;

        while (count != index - 1) {
            current = current.getNext();
            count++;
        }

        temp.setNext(current.getNext());
        current.setNext(temp);
    }

    @Override
    public void delete(int pos) {
        if (pos == 0) {
            removeFirst();
            return;
        }
        if (pos > getSize()) {
            return;
        }

        ListNode<T> current = head;

        int count = 0;

        while (count != pos - 1) {
            current = current.getNext();
            count++;
        }

        ListNode<T> toDelete = current.getNext();
        current.setNext(toDelete.getNext());
    }

    @Override
    public T get(int pos) {
        return null;
    }

    @Override
    public void removeFirst() {
        ListNode<T> current = this.head;

        if (current.getNext() == null) {
            this.head = null;
        } else {
            this.head = current.getNext();
        }

        this.length--;
    }

    @Override
    public T getFirst() {
        if (this.head!= null) {
            return (T) this.head.getData();
        }
        else {
            return null;
        }
    }

    @Override
    public int getSize() {
        int size = 0;
        Iterator<T> iterator = iterator();

        while(iterator.hasNext()) {
            size++;
            iterator.next();
        }

        return size;
    }

    public Iterator<T> iterator() {
        return new ListIterator<>(head);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        if (this.head == null) {
            result = new StringBuilder("Linked list is empty");
        } else {
            ListNode<T> current = this.head;
            int iterator = length;

            result.append("Position ").append(iterator).append(" contains -> ").append(current.getData()).append("\n");

            while (current.getNext() != null) {
                iterator--;

                current = current.getNext();
                result.append("Position ").append(iterator).append(" contains -> ").append(current.getData()).append("\n");
            }

        }

        return result.toString();
    }
}
