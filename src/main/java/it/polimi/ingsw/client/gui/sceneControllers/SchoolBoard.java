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
import java.util.Set;

public class SchoolBoard extends Pane {
    private PlayerInfo player;

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

    public void setPlayer(PlayerInfo opponent) {
        this.name.setText(opponent.getNickname());
        this.coins.setText("                                                                                              coins: " + opponent.getCoins());
        this.updateDinningRoom(opponent.getBoard().getDiningRoom());
        this.updateEntrance(opponent.getBoard().getEntrance());
        this.updateTowers(opponent.getBoard().getTowers(), opponent.getBoard().getTowerColor());
        this.updateProfessors(opponent.getBoard().getProfessorTable());
    }

    private void updateProfessors(Set<PawnColor> professors) {
        //TODO
    }

    private void updateEntrance(List<PawnColor> entrance) {
        entranceGrid.setHgap(6);
        entranceGrid.setVgap(13);

        int row = 0;
        int column = 0;

        for (int i = 0; i < entrance.size(); i++) {
            Image image = switch (entrance.get(i)) {
                case RED -> new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
                case BLUE -> new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
                case PINK -> new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
                case GREEN -> new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
                case YELLOW -> new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
                case NONE -> new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            };
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, column, row);
            column++;
            if (column % 2 == 0) {
                row++;
                column = 0;
            }
        }
    }

    private void updateTowers(int count, Tower color) {
        towers.setVgap(5);
        towers.setHgap(5);

        String imagePath = "/it.polimi.ingsw.client.gui/assets/black_tower.png";
        //imagePath += color == Tower.BLACK ? "black_tower" : "white_tower";
        //imagePath += ".png";
        int row = 0;
        int column = 0;
        for (int i = 0; i < 7; i++) {
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

    private void updateDinningRoom(DiningRoomDto opponent) {
        int i;

        board.setHgap(5);
        board.setVgap(13);

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.GREEN); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, 1 * i, 0);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.RED); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, 1 * i, 1);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.YELLOW); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, 1 * i, 2);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.PINK); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, 1 * i, 3);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.BLUE); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            board.add(imageView, 1 * i, 4);
        }
    }

    public SchoolBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/schoolboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
