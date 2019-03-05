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
        winnerText.setText(controller.generateWinMessage(controller.checkWinner()));
    }

    public void onMainMenuClicked() throws Exception {
        TicTacToeApplication.getController().resetGame();
        TicTacToeApplication.getCoordinator().showLoginScene();
    }

    public void onQuitClicked() {
        System.exit(0);
    }
}
