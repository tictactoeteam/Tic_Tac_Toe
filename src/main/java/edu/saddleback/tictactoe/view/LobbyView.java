package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This object interacts with the lobby page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LobbyView {

    private GameController controller;

    private ServerConnection conn;

    private static LobbyView instance = null;

    @FXML
    private ListView gameListView;
    @FXML
    private Button logoutButton;

    private ObservableList<String> items = FXCollections.observableArrayList();


    /**
     * Initializes the controller in the lobby view
     */
    public void initialize() {

        controller = TicTacToeApplication.getController();
        conn = ServerConnection.getInstance();
        populateTable();
        instance = this;
        System.out.println("Running lobbyview intialize");
        //FILL THE LIST VIEW WITH THE AVAILABLE GAMES
        TicTacToeApplication.setWindowSize(700, 700);

    }

    public static void updateInstance(){
        if(instance == null){
            return;
        }
        instance.populateTable();
    }

    /**
     * Logs out of the server and goes back to the login page
     */
    public void onLogoutClicked(){

        try {
            TicTacToeApplication.getCoordinator().showGameScene();
        }catch(Exception ex){
            System.out.println("Try something else");
        }

    }

    /**
     * Repopulates the listview with all current pending players connected to the server
     */
    public void populateTable(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameListView.getItems().clear();
                ObservableList<String> items = FXCollections.observableArrayList(conn.getObservableList());
                gameListView.setItems(items);
            }
        });


    }

    /**
     * Runs when a name in the listview is clicked, and attempts to start a game
     */
    public void onListClicked(){

        System.out.println("Player Name: " + gameListView.getSelectionModel().getSelectedItem().toString());

    }


}
