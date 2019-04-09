package edu.saddleback.tictactoe.observable;

import java.util.ArrayList;

public class Observable<E> {
    private ArrayList<Listener<E>> listeners;
    private E object;

    public Observable() {
        this.listeners = new ArrayList<>();
    }

    public void subscribe(Listener<E> listener) {
        if (object != null) {
            listener.update(object);
        }
        listeners.add(listener);
    }

    public void set(E object) {
        this.object = object;
        this.listeners.forEach(eListener -> eListener.update(object));
    }

    public E get() {
        return object;
    }
}
