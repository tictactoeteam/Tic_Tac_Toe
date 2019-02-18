module Tic.Tac.Toe {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    exports edu.saddleback.tictactoe;
    exports edu.saddleback.tictactoe.view; // for FXML controller loading

    opens edu.saddleback.tictactoe;
    opens edu.saddleback.tictactoe.view; // for FXML node initialization
}