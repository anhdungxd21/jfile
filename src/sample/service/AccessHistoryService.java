package sample.service;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Consumer;

public class AccessHistoryService<E> {
    private LinkedList<E> linkedList;
    public int pointer = -1;

    public AccessHistoryService() {
        this.linkedList = new LinkedList<>();
    }

    private void setPointer() {
        if (pointer == -1) {
            pointer = 0;
        }
    }

    public E currentItem() {
        return linkedList.get(pointer);
    }


    public E nextItem() {
        final int index = pointer + 1;
        final ListIterator iterator = linkedList.listIterator(index);
        if (iterator.hasNext()) {
            pointer++;
            return (E) iterator.next();
        }
        return null;
    }

    public E prevItem() {
        final int index = pointer;
        final ListIterator iterator = linkedList.listIterator(index);
        if (iterator.hasPrevious()) {
            pointer--;
            return (E) iterator.previous();
        }
        return null;
    }

    public void add(E item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.isFirstAdd(item)) {
            return;
        }
        if (this.isAddLast(item)) {
            return;
        }
        //help gc
        this.dropAndClean();
        linkedList.add(item);
        pointer = linkedList.size() - 1;
    }

    private boolean isAddLast(E item) {
        if (linkedList.getLast() == linkedList.get(pointer)) {
            linkedList.add(item);
            pointer = linkedList.size() - 1;
            return true;
        }
        return false;
    }

    private boolean isFirstAdd(E item) {
        if (linkedList.size() == 0) {
            linkedList.add(item);
            setPointer();
            return true;
        }
        return false;
    }

    private void dropAndClean() {
        E tail = linkedList.getLast();
        E pointerItem = linkedList.get(pointer);
        while (pointerItem != tail) {
            linkedList.removeLast();
            tail = linkedList.getLast();
        }
    }

    public void forEach(Consumer action) {
        linkedList.forEach(action);
    }
}
