package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.DiningRoomDto;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class EditableSchoolBoard extends Pane {
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY, startingX, startingY;

    @FXML
    private GridPane diningRoom;
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

    public void setPlayer(PlayerInfo me) {
        this.updateDinningRoom(me.getBoard().getDiningRoom());
        this.updateEntrance(me.getBoard().getEntrance());
        this.updateTowers(me.getBoard().getTowers(), me.getBoard().getTowerColor());
        /*tRed.setVisible(me.getBoard().isThereProfessor(PawnColor.RED));
        tBlue.setVisible(me.getBoard().isThereProfessor(PawnColor.BLUE));
        tGreen.setVisible(me.getBoard().isThereProfessor(PawnColor.GREEN));
        tPink.setVisible(me.getBoard().isThereProfessor(PawnColor.PINK));
        tYellow.setVisible(me.getBoard().isThereProfessor(PawnColor.YELLOW));*/
        tRed.setVisible(true);
        tBlue.setVisible(true);
        tGreen.setVisible(me.getBoard().isThereProfessor(PawnColor.GREEN));
        tPink.setVisible(me.getBoard().isThereProfessor(PawnColor.PINK));
        tYellow.setVisible(true);
    }

    //TODO fix
    private void updateEntrance(List<PawnColor> entrance) {
        entranceGrid.getChildren().removeAll(entranceGrid.getChildren());

        entranceGrid.setHgap(8);
        entranceGrid.setVgap(15);

        int row = 0;
        int column = 0;

        for (int i = 0; i < entrance.size(); i++) {
            Image image = resolveImage(entrance.get(i));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            imageView.setOnMousePressed(mouseEvent -> {
                orgSceneX = mouseEvent.getSceneX();
                orgSceneY = mouseEvent.getSceneY();
                orgTranslateX = imageView.getTranslateX();
                orgTranslateY = imageView.getTranslateY();
            });

            imageView.setOnMouseDragged(mouseEvent -> {
                double offsetX = mouseEvent.getSceneX() - orgSceneX;
                double offsetY = mouseEvent.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                imageView.setTranslateX(newTranslateX);
                imageView.setTranslateY(newTranslateY);
            });

            entranceGrid.add(imageView, column, row);

            column++;
            if (column == 2) {
                row++;
                column = 0;
            }
        }
    }

    private void updateTowers(int count, Tower color) {
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

    private void updateDinningRoom(DiningRoomDto opponent) {
        diningRoom.getChildren().removeAll(diningRoom.getChildren());

        int i;

        diningRoom.setHgap(5);
        diningRoom.setVgap(14);

        for (i = 0; i < /*opponent.getStudentsInDiningRoom(PawnColor.GREEN)*/4; i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, 1 * i, 0);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.GREEN) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setOpacity(0);
            diningRoom.add(imageView, 0, 0);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.RED); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, 0, 1);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.RED) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setOpacity(0);
            diningRoom.add(imageView, 0, 1);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.YELLOW); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, 0, 2);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.YELLOW) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setOpacity(0);
            diningRoom.add(imageView, 0, 2);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.PINK); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, 0, 3);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.PINK) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setOpacity(0);
            diningRoom.add(imageView, 0, 3);
        }

        for (i = 0; i < /*opponent.getStudentsInDiningRoom(PawnColor.BLUE)*/6; i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, 0, 4);
        }
    }

    private Image resolveImage(PawnColor p) {
        return switch (p) {
            case RED, NONE -> new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            case BLUE -> new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            case PINK -> new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
            case GREEN -> new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            case YELLOW -> new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
        };
    }

    public EditableSchoolBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/editable-schoolboard.fxml"));
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
