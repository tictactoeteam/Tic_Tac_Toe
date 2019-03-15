package edu.saddleback.tictactoe.messages;

import edu.saddleback.tictactoe.controller.ServerConnection;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;

import java.io.Serializable;
import java.util.Scanner;

public class Message implements Serializable {
    private String type;
    private Serializable data;

    public static void main(String[] args){
        ServerConnection connection = new ServerConnection();

        int choice = 0;
        Scanner input = new Scanner(System.in);

        while(choice != -1){
            System.out.println("Choose a request to send!");

            System.out.println("<1> Host Request");
            System.out.println("<2> Join Request");
            System.out.println("<3> MoveEvaluate Request");
            System.out.println("<4> Empty Request");

            choice = input.nextInt();

            switch (choice){
                case 1:
                    connection.sendRequest(Request.createHostRequest("Tomasz"));
                    break;
                case 2:
                    connection.sendRequest(Request.createJoinRequest(420));
                    break;
                case 3:
                    connection.sendRequest(Request.createMoveValidateRequest(new BoardMove(0, 0, GamePiece.X)));
                    break;
                case 4:
                    connection.sendRequest(new Request());
                    break;
                default:
                    choice = -1;
                    break;
            }

        }
    }

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
