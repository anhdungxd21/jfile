package sample.service;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Consumer;

public class AccessHistoryService<E> {
    private LinkedList<E> linkedList;
    private int pointer = -1;

    public AccessHistoryService() {
        this.linkedList = new LinkedList<>();
    }

    private void setPointer(){
        if(pointer == -1) {
            pointer = 0;
        }
    }

    public E currentItem(){
        return linkedList.get(pointer);
    }

    public E nextItem(){
        int index = pointer;
        final E next = this.next(index);
        if(next == null){
            return null;
        }
        pointer++;
        return next;
    }

    public E prevItem(){
        int index = pointer;
        final E prev = this.prev(index);
        if(prev == null){
            return null;
        }
        pointer--;
        return prev;
    }

    private E next(int index){
        int sizeList = linkedList.size();
        return index < sizeList - 1 ? linkedList.get(index+1) : null;
    }

    private E prev(int index){
        return index > 0 ? linkedList.get(index-1) : null;
    }

    public void add(E item){
        if(item == null){
            throw new IllegalArgumentException();
        }
        if(linkedList.size() == 0){
            linkedList.add(item);
            setPointer();
            return;
        }

        if (linkedList.getLast() == linkedList.get(pointer)) {
            linkedList.add(item);
            pointer = linkedList.size() - 1;
            return;
        }

        //help gc
        E tail = linkedList.getLast();
        E pointerItem = linkedList.get(pointer);
        while(pointerItem != tail){
            linkedList.removeLast();
            tail = linkedList.getLast();
        }
        linkedList.add(item);
        pointer = linkedList.size() - 1;
    }

    public void forEach(Consumer action){
        linkedList.forEach(action);
    }

    public static void main(String[] args) {
        AccessHistoryService controller = new AccessHistoryService();
        controller.add("a");
        controller.add("b");
        controller.add("c");
        controller.add("d");
        controller.add("e");
        controller.add("f");
        controller.forEach(item -> {
            System.out.print(item + ", ");
        });
        System.out.println();
        System.out.println("--------------------------------");

        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Next Item: " +controller.nextItem());
        System.out.println("Prev Item: " + controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Prev Item: " +controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Prev Item: " +controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Prev Item: " +controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Prev Item: " +controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
        System.out.println("Prev Item: " +controller.prevItem());
        System.out.println("Current Item: "+controller.currentItem());
//        System.out.println("Next Item: " +controller.nextItem());
//        System.out.println("Current Item: "+controller.currentItem());
        controller.add("h");
        controller.add("i");
        controller.add("k");

        System.out.println("--------------------------------");
        controller.forEach(item -> {
            System.out.print(item + ", ");
        });
        System.out.println();
        System.out.println("--------------------------------");
//        System.out.println(controller.nextItem());
//        controller.linkedList.forEach(System.out::println);
//        System.out.println("---------------------------");
//        controller.linkedList.remove("f");
//        controller.linkedList.remove("a");
//
//        System.out.println(controller.linkedList.get(0));
//        System.out.println(controller.linkedList.indexOf("a"));
//        controller.linkedList.forEach(System.out::println);


    }
}
