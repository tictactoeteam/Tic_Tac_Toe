package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * This object interacts with the winner UI that displays the game end state message.
 */
public class WinnerView{

    private GameController controller;
    @FXML
    Text winnerText;

    /**
     * Initializes the controller into the winner scene.
     */
    @FXML
    protected void initialize(){
        controller = TicTacToeApplication.getController();
        winnerText.setText(controller.generateWinMessage());
    }

    /**
     * Returns you to the lobby after a game is complete.
     * @throws Exception
     */
    public void onMainMenuClicked() throws Exception {
        TicTacToeApplication.getController().resetGame();
        TicTacToeApplication.getCoordinator().showLobbyScene();
    }

    /**
     * Closes the application.
     */
    public void onQuitClicked() {
        System.exit(0);
    }
}
