package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.model.enums.PawnColor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class Island extends Pane {
    @FXML
    private GridPane students;

    public Island() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/island.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setStudents(List<PawnColor> students) {
        //TODO verify this
        this.students.getChildren().removeAll(this.students.getChildren());

        this.students.setHgap(4);
        this.students.setVgap(4);

        int row = 1;
        int column = 0;

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
            if (column == 5) {
                row++;
                column = 0;
            }

            this.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getTarget().equals(this)) {
                        System.out.println("Mouse released");

                    }
                }
            });

            //TODO
            imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("Released");
                }
            });


        }
    }
}