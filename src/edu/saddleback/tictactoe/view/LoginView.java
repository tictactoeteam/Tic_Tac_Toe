package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.decision.Node;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import edu.saddleback.tictactoe.controller.GameController;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LoginView {

    private boolean mrBillGoesFirst;
    private GameController controller;

    @FXML
    private HBox radioHbox;
    @FXML
    private RadioButton radioPlayer;
    @FXML
    private RadioButton radioBill;
    @FXML
    private TextField playerName;
    @FXML
    private TextField player1Name;
    @FXML
    private TextField player2Name;
    @FXML
    private ComboBox difficultyCombo;
    @FXML
    private Label errorText;
    @FXML
    private ComboBox multiplayerComboBox;
    @FXML
    private ComboBox onlineTypeComboBox;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField joinCodeTextField;

    /**
     * Initializes the controller in the login view
     */
    public void initialize() {
        controller = TicTacToeApplication.getController();
        ToggleGroup group = new ToggleGroup();
        radioPlayer.setToggleGroup(group);
        radioBill.setToggleGroup(group);
    }

    /**
     * Controls which ui components should be displayed.
     * @param multiplayer
     */
    public void setMultiplayer(boolean multiplayer){

        player1Name.setVisible(multiplayer);
        player2Name.setVisible(multiplayer);
        multiplayerComboBox.setVisible(multiplayer);
        playerName.setVisible(!multiplayer);
        difficultyCombo.setVisible(!multiplayer);
        controller.setMultiplayer(multiplayer);
        errorText.setVisible(false);

    }

    /**
     * Sets multiplayer to false.
     */
    public void onSingleplayerClicked() {
        radioHbox.setVisible(true);
        setMultiplayer(false);
        difficultyCombo.getSelectionModel().selectFirst();
        radioPlayer.setSelected(true);
        onlineTypeComboBox.setVisible(false);
        ipTextField.setVisible(false);
        joinCodeTextField.setVisible(false);
    }

    /**
     * Sets multiplayer to true.
     */
    public void onMultiplayerClicked() {
        radioHbox.setVisible(false);
        setMultiplayer(true);
        multiplayerComboBox.getSelectionModel().selectFirst();
        onlineTypeComboBox.getSelectionModel().selectFirst();
    }


    /**
     * Triggered when the user clicks on the multiplayer type combobox, changing the needed ui
     */
    public void setMultiplayerType(){

        errorText.setVisible(false);

        //Local Option
        if(multiplayerComboBox.getSelectionModel().getSelectedIndex() == 0){


            player1Name.setVisible(true);
            player1Name.setPromptText("Player One");
            player2Name.setVisible(true);
            ipTextField.setVisible(false);
            onlineTypeComboBox.setVisible(false);
            joinCodeTextField.setVisible(false);



            //Online Option
        }else if(multiplayerComboBox.getSelectionModel().getSelectedIndex() == 1){

            player1Name.setPromptText("Player Name");
            player2Name.setVisible(false);
            onlineTypeComboBox.setVisible(true);
            ipTextField.setVisible(true);

            //Create game case
            if(onlineTypeComboBox.getSelectionModel().getSelectedIndex() == 0){

                joinCodeTextField.setVisible(false);


                //Join game case
            }else{

                joinCodeTextField.setVisible(true);

            }

        }

    }

    /**
     * Triggered when the online type combo box is updated with a new choice.
     */
    public void setOnlineType(){

        //Create game case
        if(onlineTypeComboBox.getSelectionModel().getSelectedIndex() == 0){

            joinCodeTextField.setVisible(false);

            //Join game case
        }else{

            joinCodeTextField.setVisible(true);

        }

    }

    /**
     * Places the loading screens, must be done here and not in the onPlayClicked() method because the processor is
     * overloaded generating the tree, this is the work around.
     */
    public void onPlayPressed(){

        if(controller.isMultiplayer()){

            //Local case
            if(multiplayerComboBox.getSelectionModel().getSelectedItem().toString().equals("Local")){

                if(!player1Name.getText().trim().equals("") && !player2Name.getText().trim().equals("")){

                    errorText.setText("Loading...");
                    errorText.setVisible(true);

                }

                //Online Case
            }else{

                //Create game case
                if(onlineTypeComboBox.getSelectionModel().getSelectedItem().toString().equals("Create Game")){

                    if(!player1Name.getText().trim().equals("") && !ipTextField.getText().trim().equals("")){

                        errorText.setText("Loading...");
                        errorText.setVisible(true);

                    }

                    //Join game case
                }else{

                    if(!player1Name.getText().trim().equals("") && !ipTextField.getText().trim().equals("") &&
                       !joinCodeTextField.getText().trim().equals("")){

                        errorText.setText("Loading...");
                        errorText.setVisible(true);

                    }

                }

            }

            //Singleplayer case
        }else{

            if(!difficultyCombo.getSelectionModel().isEmpty() && !playerName.getText().trim().equals("")){

                if(difficultyCombo.getValue().toString().equals("Easy Mode"))
                    errorText.setText("Loading easy AI...");
                else
                    errorText.setText("Waking Mr. Bill from his 1000 year slumber...");

                errorText.setVisible(true);

            }

        }

    }

    /**
     * Preforms all error checks to see if the user entered all needed information into the login screen, else spits
     * out an error message. If all is correct, the specified game is generated.
     * @throws Exception
     */
    public void onPlayClicked() throws Exception {

        mrBillGoesFirst = radioBill.isSelected();

        if (controller.isMultiplayer()) {

            //Local case
            if(multiplayerComboBox.getSelectionModel().getSelectedItem().toString().equals("Local")){

                controller.setPlayer1Name(player1Name.getText());
                controller.setPlayer2Name(player2Name.getText());
                TicTacToeApplication.getCoordinator().showGameScene();
                if(!player1Name.getText().trim().equals("") && !player2Name.getText().trim().equals("")){

                    controller.setPlayer1Name(player1Name.getText());
                    controller.setPlayer2Name(player2Name.getText());
                    TicTacToeApplication.getCoordinator().showGameScene();
                }else{
                    errorText.setText("***error - please enter all info***");
                    errorText.setVisible(true);
                }

                controller.setLocalMultiplayerUp();

                //Online case
            }else{

                //Create game case
                if(onlineTypeComboBox.getSelectionModel().getSelectedItem().toString().equals("Create Game")){
                    if(!player1Name.getText().trim().equals("") && !ipTextField.getText().trim().equals("")){
                        controller.setPlayer1Name(player1Name.getText());
                        controller.setIP(ipTextField.getText());
                        TicTacToeApplication.getCoordinator().showGameScene();
                    }else{

                        errorText.setText("***error - please enter all info***");
                        errorText.setVisible(true);
                    }

                    controller.setOnlineUp(true);

                    //Join game case
                }else{

                    if(!player1Name.getText().trim().equals("") && !ipTextField.getText().trim().equals("") &&
                       !joinCodeTextField.getText().trim().equals("")){
                        controller.setPlayer2Name(player1Name.getText());
                        controller.setIP(ipTextField.getText());
                        TicTacToeApplication.getCoordinator().showGameScene();
                    }else{
                        errorText.setText("***error - please enter all info***");
                        errorText.setVisible(true);
                    }

                    controller.setOnlineUp(false);
                }
            }

            //Singleplayer case
        } else{
            if(!difficultyCombo.getSelectionModel().isEmpty() && !playerName.getText().trim().equals("")){
                if (mrBillGoesFirst) {

                    controller.setPlayer1Name("Mr. Bill");
                    controller.setPlayer2Name(playerName.getText());
                }else{
                    controller.setPlayer2Name("Mr. Bill");
                    controller.setPlayer1Name(playerName.getText());
                }

                controller.awakenMrBill(new Node());
                controller.setDifficulty(difficultyCombo.getValue().toString());

                controller.setSinglePlayerUp(mrBillGoesFirst);

                TicTacToeApplication.getCoordinator().showGameScene();

                controller.notifyBoard();

            }else{

                errorText.setText("***error - please enter all info***");
                errorText.setVisible(true);

            }

        }

    }

}