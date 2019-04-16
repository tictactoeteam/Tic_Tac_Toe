package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.io.IOException;

/**
 * This object interacts with the lobby page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LobbyView {

    private GameController controller;
    @FXML
    private ListView gameListView;
    @FXML
    private Button logoutButton;

    /**
     * Initializes the controller in the lobby view
     */
    public void initialize() {

        controller = TicTacToeApplication.getController();
        gameListView = new ListView();
        //FILL THE LIST VIEW WITH THE AVAILABLE GAMES
        TicTacToeApplication.setWindowSize(700, 700);

    }

    /**
     * Logs out of the server and goes back to the login page
     */
    public void onLogoutClicked(){

        //Kill server connection
        try {
            TicTacToeApplication.getCoordinator().showGameScene();
        }catch(Exception ex){
            System.out.println("Try something else");
        }

//        try{
//
//            TicTacToeApplication.getCoordinator().showLoginScene();
//
//        }catch(IOException ex){
//            System.out.println("Big 'ol OOF");
//        }

    }

}
