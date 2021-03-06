package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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
    @FXML
    private Button historyButton;
    @FXML
    private Button challengeMrBill;
    private ObservableList<String> items = FXCollections.observableArrayList();


    /**
     * Initializes the controller in the lobby view.
     */
    public void initialize() {

        controller = TicTacToeApplication.getController();
        conn = ServerConnection.getInstance();

        conn.getGameStartObservable().subscribe((onGameStarted) ->{

            if (onGameStarted.equals(true)) {
                try {
                    TicTacToeApplication.getCoordinator().showGameScene();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        populateTable();
        instance = this;
        System.out.println("Running lobbyview intialize");
        //FILL THE LIST VIEW WITH THE AVAILABLE GAMES
        TicTacToeApplication.setWindowSize(700, 700);

    }

    /**
     * Allows for dynamic lobbytable updates.
     */
    public static void updateInstance(){
        if(instance == null){
            return;
        }
        instance.populateTable();
    }

    /**
     * Logs out of the server and goes back to the login page
     */
    public void onLogoutClicked(){System.exit(0);}

    /**
     * Repopulates the listview with all current pending players connected to the server
     */
    public void populateTable(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameListView.getItems().clear();
                ObservableList<String> items = FXCollections.observableArrayList(conn.getObservableList());

                ObservableList<String> usernames = FXCollections.observableArrayList();
                for (String id : items){

                    if (!(conn.getUsername(id) == null))
                        usernames.add(conn.getUsername(id));
                }
                gameListView.setItems(usernames);
            }
        });

    }

    /**
     * Runs when a name in the listview is clicked, and attempts to start a game
     */
    public void onListClicked(){

        conn.challenge(gameListView.getSelectionModel().getSelectedItem().toString(), conn.getAttemptedUsername());

    }

    /**
     * Shows the scene that will show all old games, the results, and their moves.
     */
    public void onHistoryClicked(){

        try {
            TicTacToeApplication.getCoordinator().showHistoryScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Challenges Mr. Bill to a game
     */
    public  void onMrBillClicked(){

        conn.challenge("Mr. Bill", conn.getAttemptedUsername());
        conn.setVisibleInLobby(false);

    }

}
