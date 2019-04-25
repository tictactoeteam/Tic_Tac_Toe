package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.application.Platform;

public class EndStateHandler implements MessageHandler {
    private GameController controller;

    public EndStateHandler(GameController controller){

        this.controller = controller;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

        //True if Winner and Loser, false if draw
        if (data.has("winner")) {

            String winnerName = data.get("winner").getAsString();
            String loserName = data.get("loser").getAsString();

            //True if this is your game and player 1 won
            if((controller.getPlayer1Name().equals(winnerName) && controller.getPlayer2Name().equals(loserName)) ||
                (controller.getPlayer1Name().equals(loserName) && controller.getPlayer2Name().equals(winnerName))) {

                controller.setWinnerName(loserName);
                controller.setLoserName(winnerName);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TicTacToeApplication.getCoordinator().showWinnerScene();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }else{//Draw

            String p1Name = data.get("player1").getAsString();
            String p2Name = data.get("player2").getAsString();

            //True if this is your game
            if((controller.getPlayer1Name().equals(p1Name) && controller.getPlayer2Name().equals(p2Name)) ||
                    (controller.getPlayer1Name().equals(p2Name) && controller.getPlayer2Name().equals(p1Name))){

                controller.setWinnerName("DRAW");
                controller.setLoserName("DRAW");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TicTacToeApplication.getCoordinator().showWinnerScene();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }

    }
}
