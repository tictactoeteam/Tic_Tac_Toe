package edu.saddleback.tictactoe.observable;

public interface Listener<E> {
    void update(E message);
}
