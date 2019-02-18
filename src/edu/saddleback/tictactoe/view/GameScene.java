package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameScene {
    private Group root;

    public GameScene(GameController controller) {
        root = new Group();
        StackPane gameStack = new StackPane();
        gameStack.setAlignment(Pos.CENTER);
        gameStack.setPrefSize(600, 400);

        //Holds the user's name(s) on top, then the controller, then space to display the winner
        VBox gameUIVBox = new VBox();
        gameUIVBox.setAlignment(Pos.CENTER);

        //Shows the player(s) and who's turn it is.
        Text playerNamesText = new Text();

        //Holds the entire controller ui
        BoardView boardView = new BoardView(controller);
        boardView.setAlignment(Pos.CENTER);


        //Shows the winner or tie when the game reaches end state.
        Text winnerText = new Text();
        winnerText.setVisible(false);

        gameUIVBox.getChildren().addAll(playerNamesText, boardView, winnerText);

        gameStack.getChildren().add(gameUIVBox);
        root.getChildren().add(gameStack);
    }

    public Group getRoot() {
        return root;
    }
}
