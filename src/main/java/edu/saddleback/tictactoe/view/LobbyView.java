package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.controller.ServerConnection;
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
        System.out.println("Running lobbyview intialize");
        //FILL THE LIST VIEW WITH THE AVAILABLE GAMES
        TicTacToeApplication.setWindowSize(700, 700);

    }

    /**
     * Logs out of the server and goes back to the login page
     */
    public void onLogoutClicked(){

        //Kill server connection
        populateTable();

//        try {
//            TicTacToeApplication.getCoordinator().showGameScene();
//        }catch(Exception ex){
//            System.out.println("Try something else");
//        }

    }

    public void populateTable(){
        ObservableList<String> items =FXCollections.observableArrayList (conn.getObservableList());
        gameListView.setItems(items);
    }


}
