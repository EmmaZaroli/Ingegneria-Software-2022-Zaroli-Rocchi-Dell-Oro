package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.DiningRoomDto;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchoolBoard extends Pane {
    private PlayerInfo player;

    @FXML
    private boolean isExpertGame;
    @FXML
    private Label name;
    @FXML
    private Label coins;
    @FXML
    private GridPane board;
    @FXML
    private GridPane towers;
    @FXML
    private GridPane entranceGrid;
    @FXML
    private ImageView tRed;
    @FXML
    private ImageView tBlue;
    @FXML
    private ImageView tGreen;
    @FXML
    private ImageView tPink;
    @FXML
    private ImageView tYellow;

    /**
     * Sets the player
     *
     * @param opponent The player owner of the schoolboard
     */
    public void setPlayer(PlayerInfo opponent) {
        this.name.setText(opponent.getNickname());
        this.coins.setText("                                                                                              coins: " + opponent.getCoins());
        this.updateDinningRoom(opponent.getBoard().getDiningRoom());
        this.updateEntrance(opponent.getBoard().getEntrance());
        this.updateTowers(opponent.getBoard().getTowers(), opponent.getBoard().getTowerColor());
        tRed.setVisible(opponent.getBoard().isThereProfessor(PawnColor.RED));
        tBlue.setVisible(opponent.getBoard().isThereProfessor(PawnColor.BLUE));
        tGreen.setVisible(opponent.getBoard().isThereProfessor(PawnColor.GREEN));
        tPink.setVisible(opponent.getBoard().isThereProfessor(PawnColor.PINK));
        tYellow.setVisible(opponent.getBoard().isThereProfessor(PawnColor.YELLOW));
    }

    /**
     * Sets whether the game is in expert mode
     *
     * @param isExpert
     */
    public void setIsExpert(boolean isExpert) {
        this.isExpertGame = isExpert;
        coins.setVisible(isExpert);
    }

    /**
     * Updates the displayed entrance
     */
    private void updateEntrance(List<PawnColor> entrance) {
        entranceGrid.getChildren().removeAll(entranceGrid.getChildren());
        entranceGrid.setHgap(7);
        entranceGrid.setVgap(13);

        int row = 0;
        int column = 0;

        for (int i = 0; i < entrance.size(); i++) {
            Image image = switch (entrance.get(i)) {
                case RED, NONE -> new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
                case BLUE -> new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
                case PINK -> new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
                case GREEN -> new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
                case YELLOW -> new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
            };
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            entranceGrid.add(imageView, column, row);
            column++;
            if (column % 2 == 0) {
                row++;
                column = 0;
            }
        }
    }

    /**
     * Updates the displayed towers
     */
    private void updateTowers(int count, Tower color) {
        towers.getChildren().removeAll(towers.getChildren());
        towers.setVgap(5);
        towers.setHgap(5);

        String imagePath = "/it.polimi.ingsw.client.gui/assets/black_tower.png";

        if (color == Tower.WHITE) {
            imagePath = "/it.polimi.ingsw.client.gui/assets/white_tower.png";
        }

        if (color == Tower.GREY) {
            imagePath = "/it.polimi.ingsw.client.gui/assets/grey_tower.png";
        }

        int row = 0;
        int column = 0;
        for (int i = 0; i < count; i++) {
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(29);
            imageView.setFitWidth(14);
            towers.add(imageView, column, row);
            column = (column + 1) % 3;
            if (column == 0) {
                row++;
            }
        }
    }

    /**
     * Updates the displayed dinning room
     */
    private void updateDinningRoom(DiningRoomDto opponent) {
        board.getChildren().removeAll(board.getChildren());

        int i;

        board.setHgap(5);
        board.setVgap(15);

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.GREEN); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, i, 0);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.GREEN) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            imageView.setOpacity(0);
            board.add(imageView, 0, 0);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.RED); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, i, 1);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.RED) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            imageView.setOpacity(0);
            board.add(imageView, 0, 1);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.YELLOW); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, i, 2);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.YELLOW) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            imageView.setOpacity(0);
            board.add(imageView, 0, 2);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.PINK); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, i, 3);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.PINK) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            imageView.setOpacity(0);
            board.add(imageView, 0, 3);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.BLUE); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, i, 4);
        }
    }

    /**
     * Creates a new schoolboard
     */
    public SchoolBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/schoolboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error in Schoolboard", exception);
        }
    }
}
