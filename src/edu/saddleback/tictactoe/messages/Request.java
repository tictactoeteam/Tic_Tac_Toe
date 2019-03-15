package edu.saddleback.tictactoe.messages;

import edu.saddleback.tictactoe.model.BoardMove;

import java.io.Serializable;

public class Request extends Message {
    public Request(){
        this("EmptyRequest", null);
    }

    public Request(String type, Serializable data){
        super(type, data);
    }

    public static Request createHostRequest(String playerName){
        return new Request("Host", playerName);
    }

    public static Request createJoinRequest(Integer joinCode){
        return new Request("Join", joinCode);
    }

    public static Request createMoveValidateRequest(BoardMove boardMove){
        return new Request("MoveValidate", boardMove);
    }

}
