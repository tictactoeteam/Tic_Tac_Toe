package edu.saddleback.tictactoe.observable;

/**
 * Listener Interface
 * @param <E>
 */
public interface Listener<E> {
    void update(E message);
}
