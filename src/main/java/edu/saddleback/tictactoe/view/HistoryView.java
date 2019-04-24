package edu.saddleback.tictactoe.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Arrays;



public class HistoryView {

    private ServerConnection conn;

    @FXML
    private TableView<TableViewObject> moveTable;
    @FXML
    private ListView gameTable;
    @FXML
    private Button lobbyButton;

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
     * When a game is selected the players and the moves, in order, will be shown in the TableView.
     */
    public void onGameClicked(){

        try{
            String gameClicked = gameTable.getSelectionModel().getSelectedItem().toString();
            int i = 0;
            int foundIndex = -1;
            while(i < playerXNames.size() && foundIndex == -1){

                if(gameClicked.contains(playerXNames.get(i)) && gameClicked.contains(playerONames.get(i))){
                    foundIndex = i;
                }
                i++;

            }
            //foundIndex now contains the correct selected game's index

            //Fills the table with the data
            ObservableList<TableViewObject> data = FXCollections.observableArrayList();
            for(int j = 0; j < allGameMoves.get(foundIndex).size(); j++){
                data.add(new TableViewObject(j, allGameMoves.get(foundIndex).get(j).intValue()));
            }
            moveTable.setItems(data);

            //Assigns the columns their data
            TableColumn<TableViewObject, Integer> moveNumberCol = new TableColumn("Move Number");
            moveNumberCol.setMinWidth(100);
            moveNumberCol.setCellValueFactory(new PropertyValueFactory<>("moveNumberValue"));
            TableColumn<TableViewObject, Integer> moveDataCol = new TableColumn<>("Move Index");
            moveDataCol.setMinWidth(100);
            moveDataCol.setCellValueFactory(new PropertyValueFactory<>("moveIndexValue"));
            moveTable.getColumns().addAll(moveNumberCol, moveDataCol);

        }catch(NullPointerException ex){}

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

    /**
     * Object for filling the tableview.
     */
    public static class TableViewObject {
        private final SimpleIntegerProperty moveNumberProperty ;
        private final SimpleIntegerProperty moveIndexProperty ;
        public TableViewObject(Integer moveNum, Integer moveIndex) {
            this.moveNumberProperty = new SimpleIntegerProperty(moveNum) ;
            this.moveIndexProperty = new SimpleIntegerProperty(moveIndex);
        }
        public SimpleIntegerProperty getMoveNumberValue() {
            return moveNumberProperty;
        }
        public SimpleIntegerProperty getMoveIndexValue() {
            return moveIndexProperty;
        }
    }

}

