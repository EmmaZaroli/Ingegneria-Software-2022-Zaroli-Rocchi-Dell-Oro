package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.gui.Gui;
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

/**
 * a class to represent a schoolboard that supports drag and drop
 */
public class EditableSchoolBoard extends Pane {
    private Gui gui;
    private boolean isDragAndDropEnabled = false;
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

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

    /**
     * Sets the player
     *
     * @param me The player owner of the schoolboard
     */
    public void setPlayer(PlayerInfo me) {
        this.updateDinningRoom(me.getBoard().getDiningRoom());
        this.updateEntrance(me.getBoard().getEntrance());
        this.updateTowers(me.getBoard().getTowers(), me.getBoard().getTowerColor());
        tRed.setVisible(me.getBoard().isThereProfessor(PawnColor.RED));
        tBlue.setVisible(me.getBoard().isThereProfessor(PawnColor.BLUE));
        tGreen.setVisible(me.getBoard().isThereProfessor(PawnColor.GREEN));
        tPink.setVisible(me.getBoard().isThereProfessor(PawnColor.PINK));
        tYellow.setVisible(me.getBoard().isThereProfessor(PawnColor.YELLOW));
    }

    /**
     * Updates the displayed entrance
     *
     * @param entrance Students to place in the entrance
     */
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
                if (isDragAndDropEnabled) {
                    orgSceneX = mouseEvent.getSceneX();
                    orgSceneY = mouseEvent.getSceneY();
                    orgTranslateX = imageView.getTranslateX();
                    orgTranslateY = imageView.getTranslateY();
                }
            });

            final int j = i;

            imageView.setOnMouseReleased(mouseEvent -> {
                if (isDragAndDropEnabled) {
                    double x = mouseEvent.getScreenX();
                    double y = mouseEvent.getScreenY();
                    if (isInRange(x, y, 158, 404)) {
                        gui.sendToIsland(entrance.get(j), 0);
                        return;
                    }
                    if (isInRange(x, y, 324, 317)) {
                        gui.sendToIsland(entrance.get(j), 1);
                        return;
                    }
                    if (isInRange(x, y, 482, 316)) {
                        gui.sendToIsland(entrance.get(j), 2);
                        return;
                    }
                    if (isInRange(x, y, 648, 319)) {
                        gui.sendToIsland(entrance.get(j), 3);
                        return;
                    }
                    if (isInRange(x, y, 814, 318)) {
                        gui.sendToIsland(entrance.get(j), 4);
                        return;
                    }
                    if (isInRange(x, y, 971, 324)) {
                        gui.sendToIsland(entrance.get(j), 5);
                        return;
                    }
                    if (isInRange(x, y, 323, 492)) {
                        gui.sendToIsland(entrance.get(j), 6);
                        return;
                    }
                    if (isInRange(x, y, 483, 491)) {
                        gui.sendToIsland(entrance.get(j), 7);
                        return;
                    }
                    if (isInRange(x, y, 640, 493)) {
                        gui.sendToIsland(entrance.get(j), 8);
                        return;
                    }
                    if (isInRange(x, y, 808, 493)) {
                        gui.sendToIsland(entrance.get(j), 9);
                        return;
                    }
                    if (isInRange(x, y, 973, 494)) {
                        gui.sendToIsland(entrance.get(j), 10);
                        return;
                    }
                    if (isInRange(x, y, 1132, 408)) {
                        gui.sendToIsland(entrance.get(j), 11);
                        return;
                    }
                    if (x > 647 && y > 630 && x < 893 && y < 810) {
                        //TODO check if posible
                        gui.sendToBoard(entrance.get(j));
                        return;
                    }
                    gui.print();
                }
            });

            imageView.setOnMouseDragged(mouseEvent -> {
                if (isDragAndDropEnabled) {
                    double offsetX = mouseEvent.getSceneX() - orgSceneX;
                    double offsetY = mouseEvent.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    imageView.setTranslateX(newTranslateX);
                    imageView.setTranslateY(newTranslateY);
                }
            });

            entranceGrid.add(imageView, column, row);

            column++;
            if (column == 2) {
                row++;
                column = 0;
            }
        }
    }

    /**
     * Updates the displayed towers
     *
     * @param count Number of towers to display
     * @param color Color of the towers
     */
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

        diningRoom.setHgap(4);
        diningRoom.setVgap(14);

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.GREEN); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, i, 0);
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
            diningRoom.add(imageView, i, 1);
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
            diningRoom.add(imageView, i, 2);
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
            diningRoom.add(imageView, i, 3);
        }

        if (opponent.getStudentsInDiningRoom(PawnColor.PINK) == 0) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setOpacity(0);
            diningRoom.add(imageView, 0, 3);
        }

        for (i = 0; i < opponent.getStudentsInDiningRoom(PawnColor.BLUE); i++) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            diningRoom.add(imageView, i, 4);
        }
    }

    /**
     * Returns the image of a pawn given its color
     */
    private Image resolveImage(PawnColor p) {
        return switch (p) {
            case RED, NONE -> new Image("/it.polimi.ingsw.client.gui/assets/student_red.png");
            case BLUE -> new Image("/it.polimi.ingsw.client.gui/assets/student_blue.png");
            case PINK -> new Image("/it.polimi.ingsw.client.gui/assets/student_pink.png");
            case GREEN -> new Image("/it.polimi.ingsw.client.gui/assets/student_green.png");
            case YELLOW -> new Image("/it.polimi.ingsw.client.gui/assets/student_yellow.png");
        };
    }

    /**
     * Enables the drag and drop
     */
    public void enableDragAndDrop() {
        this.isDragAndDropEnabled = true;
    }

    /**
     * Disables the drag and drop
     */
    public void disableDragAndDrop() {
        this.isDragAndDropEnabled = false;
    }

    /**
     * Sets a reference to the controller of the main application
     *
     * @param gui
     */
    public void setController(Gui gui) {
        this.gui = gui;
    }

    /**
     * Checks if a point x, y is included in the square of edge 150 starting at startX, startY
     *
     * @return true if the point is included
     */
    private boolean isInRange(double x, double y, int startX, int startY) {
        return x > startX && x < (startX + 150) && y > startY && y < (startY + 150);
    }

    /**
     * Creates an EditableSchoolBoard
     */
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
