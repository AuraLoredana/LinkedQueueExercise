package com.example.javaconcurency;

import net.jcip.annotations.ThreadSafe;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class LinkedQueue<E> {
    private int size = 0;

    public static class Node<E> {
        final E item;
        final AtomicReference<Node<E>> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<Node<E>>(next);
        }
    }

    private final Node<E> dummy = new Node<E>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<Node<E>>(dummy);

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public boolean put(E item) {
        Node<E> newNode = new Node<E>(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();
            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    // In quiescent state, try inserting new node
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(curTail, newNode);
                        size++;
                        return true;
                    }
                }
            }
        }
    }

    public E pop() {
        Node<E> curHead = head.get();
        Node<E> headNext = curHead.next.get();
        while (true) {
            if (headNext != null) {
                head.compareAndSet(curHead, headNext);
                size--;
                return headNext.item;

            } else if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        }

    }
}