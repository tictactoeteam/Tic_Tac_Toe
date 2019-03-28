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

    public Request(String type){super(type);}

    public static Request createHostRequest(String playerName){
        return new Request("Host", playerName);
    }

    public static Request createJoinRequest(String playerName, Integer joinCode){
        return new Request("Join", new Serializable[]{playerName, joinCode});
    }

    public static Request createSinglePlayerRequest(String playerName, boolean mrBillGoesFirst){
        return new Request("SinglePlayer", new Serializable[]{playerName, mrBillGoesFirst});
    }

    public static Request createMoveValidateRequest(BoardMove boardMove, int gameId){
        return new Request("MoveValidate", new Serializable[]{boardMove, gameId});
    }

    public static Request createLocalMultiplayerRequest(String playerName1, String playerName2){
        return new Request("LocalMultiplayer", new String[]{playerName1, playerName2});
    }

    public static Request createResetRequest(Integer gameId){return new Request("Reset", gameId);}

}
