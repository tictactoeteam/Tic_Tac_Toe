package edu.saddleback.tictactoe.messages;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Message implements Serializable {
    private String type;
    private Serializable data;

    public Message(){
        this("EmptyMessage", null);
    }

    public Message(String type, Serializable data){
        this.type = type;
        this.data = data;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public Serializable getData(){
        return data;
    }
    public void setData(Serializable data){
        this.data = data;
    }

}
