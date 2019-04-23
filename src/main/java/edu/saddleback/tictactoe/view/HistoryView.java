package edu.saddleback.tictactoe.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryView {

    private ServerConnection conn;

    @FXML
    private TableView moveTable;
    @FXML
    private ListView gameTable;
    @FXML
    private Button lobbyButton;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label loserLabel;

    private ArrayList<String> playerXNames;
    private ArrayList<String> playerONames;
    private ArrayList<ArrayList<Byte>> allGameMoves;

    public void initialize(){

        playerXNames = new ArrayList<>();
        playerONames = new ArrayList<>();
        allGameMoves = new ArrayList<>();

        conn = ServerConnection.getInstance();

        conn.getPubNub().addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {}
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();//Message type
                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

                if (!Arrays.asList("returnedGames").contains(type)) {
                    return;
                }

                JsonArray playerXAr = data.get("playerX").getAsJsonArray();
                JsonArray playerOAr = data.get("playerO").getAsJsonArray();
                JsonArray gameMoves = data.get("moves").getAsJsonArray();

                for(int i = 0; i < playerXAr.size(); i++){

                    playerXNames.add(playerXAr.get(i).getAsString());
                    playerONames.add(playerOAr.get(i).getAsString());
                    JsonArray tmpAr = gameMoves.get(i).getAsJsonArray();
                    ArrayList<Byte> tmpByteAR = new ArrayList<>();
                    for(int j = 0; j< 9; j++){
                        tmpByteAR.add(tmpAr.get(j).getAsByte());
                    }
                    allGameMoves.add(tmpByteAR);

                }

                ObservableList<String> items = FXCollections.observableArrayList();
                for(int i = 0; i < playerXAr.size(); i++){
                    items.setAll(playerXAr.get(i) + " verses " + playerOAr.get(i));
                }
                gameTable.setItems(items);

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
        });


        conn.getAllGamesMessage();

        //FILL LIST WITH GAMEDAO getAllGames()

    }

    /**
     * When a game is selected, the moves, winner, and loser will be shown in the TableView
     */
    public void onGameClicked(){



    }

    /**
     * Returns to the lobby scene.
     */
    public void onLobbyClicked(){

        try {
            TicTacToeApplication.getCoordinator().showLobbyScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
