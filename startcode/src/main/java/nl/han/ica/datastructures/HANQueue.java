package nl.han.ica.datastructures;

public class HANQueue<T> implements IHANQueue<T>{
    private final HANLinkedList<T> linkedList;

    public HANQueue() {
        this.linkedList = new HANLinkedList<>();
    }

    @Override
    public void clear() {
        linkedList.clear();
    }

    @Override
    public boolean isEmpty() {
        return linkedList.getSize() == 0;
    }

    @Override
    public void enqueue(T value) {
        linkedList.insert(linkedList.getSize(), value);
    }

    @Override
    public T dequeue() {
        T tmp = linkedList.getFirst();
        linkedList.removeFirst();

        return tmp;
    }

    @Override
    public T peek() {
        return linkedList.getFirst();
    }

    @Override
    public int getSize() {
        return linkedList.getSize();
    }
}
