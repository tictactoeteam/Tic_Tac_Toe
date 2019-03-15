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

    public static Request createHostRequest(){
        Request request = new Request();
        request.setType("Host");
        return request;
    }

    public static Request createJoinRequest(Integer joinCode){
        Request request = new Request();
        request.setType("Join");
        request.setData(joinCode);
        return request;
    }

    public static Request createMoveValidationRequest(BoardMove boardMove){
        Request request = new Request();
        request.setType("MoveValudate");
        request.setData(boardMove);
        return request;
    }

}
