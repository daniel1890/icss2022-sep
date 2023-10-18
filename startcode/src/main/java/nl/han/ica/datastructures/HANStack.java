package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T>{
    private final HANLinkedList<T> linkedList;

    public HANStack() {
        this.linkedList = new HANLinkedList<>();
    }

    @Override
    public void push(T value) {
        linkedList.addFirst(value);
    }

    @Override
    public T pop() {
        T temp = linkedList.getFirst();
        linkedList.removeFirst();

        return temp;
    }

    @Override
    public T peek() {
        return linkedList.getFirst();
    }
}
