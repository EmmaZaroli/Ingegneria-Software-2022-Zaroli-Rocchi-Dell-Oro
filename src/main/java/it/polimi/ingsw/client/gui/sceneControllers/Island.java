package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class Island extends Pane {
    @FXML
    private GridPane students;

    @FXML
    private ImageView image;

    private int kind;

    /**
     * Creates an island
     *
     * @param kind
     */
    public Island(@NamedArg("kind") int kind) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/island.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.kind = kind;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Callback method to load an image based on the selected kind
     */
    public void initialize() {
        if (kind == 0) {
            image.setImage(new Image("/it.polimi.ingsw.client.gui/assets/island1.png"));
        }
        if (kind == 1) {
            image.setImage(new Image("/it.polimi.ingsw.client.gui/assets/island2.png"));
        }
        if (kind == 2) {
            image.setImage(new Image("/it.polimi.ingsw.client.gui/assets/island3.png"));
        }
    }

    /**
     * Sets the infos about this cloud
     */
    public void setCloud(LinkedIslands island) {
        this.students.getChildren().removeAll(this.students.getChildren());

        List<PawnColor> students = island.getIsland().getStudents();

        this.students.setHgap(4);
        this.students.setVgap(4);

        int row = 0;
        int column = 0;

        if (island.getIsland().isHasMotherNature()) {
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/mother_nature.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(35);
            this.students.add(imageView, column, row);
            column++;
        }

        Tower tower = island.getIsland().getTower();
        if (tower != Tower.NONE) {
            Image image = switch (tower) {
                case GREY -> new Image("/it.polimi.ingsw.client.gui/assets/grey_tower.png");
                case BLACK -> new Image("/it.polimi.ingsw.client.gui/assets/black_tower.png");
                default -> new Image("/it.polimi.ingsw.client.gui/assets/white_tower.png");
            };
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(29);
            imageView.setFitWidth(14);
            this.students.add(imageView, column, row);
            column++;
        }

        for (int i = 0; i < students.size(); i++) {
            String basePath = "/it.polimi.ingsw.client.gui/assets/student_";
            String path = switch (students.get(i)) {
                case PINK -> "pink.png";
                case YELLOW -> "yellow.png";
                case GREEN -> "green.png";
                case RED -> "red.png";
                case BLUE -> "blue.png";
                case NONE -> "none.png";
            };
            Image image = new Image(basePath + path);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            this.students.add(imageView, column, row);
            column++;
            if (column == 4) {
                row++;
                column = 0;
            }
        }
    }
}