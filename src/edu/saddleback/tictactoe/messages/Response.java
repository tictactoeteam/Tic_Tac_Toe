package edu.saddleback.tictactoe.messages;

import edu.saddleback.tictactoe.model.Board;

import java.io.Serializable;

public class Response extends Message {

    public Response(){
        this("EmptyResponse", null);
    }

    public Response(String type, Serializable data){
        super(type, data);
    }

    // Responses fired throughout the game
    public static Response createMoveValidResponse(Board board){
        return new Response("MoveValid", board);
    }

    public static Response createNotYourTurnResponse(Board board){
        return new Response("NotYourTurn", board);
    }

    public static Response createMoveInvalidResponse(Board board){
        return new Response("MoveInvalid", board);
    }

    public static Response createYouWinResponse(String opponentName){
        return new Response("YouWin", opponentName);
    }

    public static Response createYouLostResponse(String opponentName){
        return new Response("YouLost", opponentName);
    }

    public static Response createGameDrawnResponse(){
        return new Response("GameDrawn", null);
    }


    // Responses fired when trying to establish connection
    public static Response createHostSuccessResponse(Integer joinCode){
        return new Response("HostSuccess", joinCode);
    }

    public static Response createHostErrorResponse(String reason){
        return new Response("HostError", reason);
    }

    public static Response createJoinSuccessResponse(String opponentName){
        return new Response("JoinSuccess", opponentName);
    }

    public static Response createJoinErrorResponse(String reason){
        return new Response("JoinError", reason);
    }

    public static Response createGameBeginsResponse(String[] playerNames){
        return new Response("GameBegins", playerNames);
    }

    public static Response createConnectionErrorResponse(String reason){
        return  new Response("ConnectionError", reason);
    }


}
