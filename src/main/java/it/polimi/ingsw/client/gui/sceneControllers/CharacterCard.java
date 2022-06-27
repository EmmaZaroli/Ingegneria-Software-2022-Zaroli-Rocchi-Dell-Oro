package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.enums.Character;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CharacterCard extends Pane {
    private ViewCharacterCard characterCard;

    private Gui gui;

    private boolean isDragAndDropEnabled = false;
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

    @FXML
    private ImageView imageView;
    @FXML
    private ImageView coin;
    @FXML
    private GridPane students;

    public CharacterCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/character-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setController(Gui gui) {
        this.gui = gui;
    }

    public void setCard(ViewCharacterCard card) {
        this.characterCard = card;
        Image image = switch (characterCard.getCharacter()) {
            case CHARACTER_ONE -> new Image("/it.polimi.ingsw.client.gui/assets/character1.jpg");
            case CHARACTER_TWO -> new Image("/it.polimi.ingsw.client.gui/assets/character2.jpg");
            case CHARACTER_FOUR -> new Image("/it.polimi.ingsw.client.gui/assets/character4.jpg");
            case CHARACTER_SIX -> new Image("/it.polimi.ingsw.client.gui/assets/character6.jpg");
            case CHARACTER_SEVEN -> new Image("/it.polimi.ingsw.client.gui/assets/character7.jpg");
            case CHARACTER_EIGHT -> new Image("/it.polimi.ingsw.client.gui/assets/character8.jpg");
            case CHARACTER_NINE -> new Image("/it.polimi.ingsw.client.gui/assets/character9.jpg");
            case CHARACTER_ELEVEN -> new Image("/it.polimi.ingsw.client.gui/assets/character11.jpg");
        };

        students.getChildren().removeAll(students.getChildren());

        int column = 0;
        int row = 1;

        for (int i = 0; i < card.getStudents().size(); i++) {
            String basePath = "/it.polimi.ingsw.client.gui/assets/student_";
            String path = switch (card.getStudents().get(i)) {
                case PINK -> "pink.png";
                case YELLOW -> "yellow.png";
                case GREEN -> "green.png";
                case RED -> "red.png";
                case BLUE -> "blue.png";
                case NONE -> "none.png";
            };
            Image img = new Image(basePath + path);
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);

            imageView.setOnMousePressed(mouseEvent -> {
                if (isDragAndDropEnabled) {
                    orgSceneX = mouseEvent.getSceneX();
                    orgSceneY = mouseEvent.getSceneY();
                    orgTranslateX = imageView.getTranslateX();
                    orgTranslateY = imageView.getTranslateY();
                }
            });

            final int j = i;

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

            imageView.setOnMouseReleased(getReleasedCallback(card.getCharacter(), i));
            this.students.add(imageView, column, row);
            column++;
            if (column == 2) {
                row++;
                column = 0;
            }
        }

        imageView.setImage(image);
        coin.setVisible(card.hasCoin());
    }

    private EventHandler<MouseEvent> getReleasedCallback(Character character, int j) {
        return null;
        /*return switch (character) {
            case CHARACTER_ONE -> new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isDragAndDropEnabled) {
                        double x = mouseEvent.getScreenX();
                        double y = mouseEvent.getScreenY();
                        if (isInRange(x, y, 158, 404)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 0);
                            return;
                        }
                        if (isInRange(x, y, 324, 317)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 1);
                            return;
                        }
                        if (isInRange(x, y, 482, 316)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 2);
                            return;
                        }
                        if (isInRange(x, y, 648, 319)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 3);
                            return;
                        }
                        if (isInRange(x, y, 814, 318)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 4);
                            return;
                        }
                        if (isInRange(x, y, 971, 324)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 5);
                            return;
                        }
                        if (isInRange(x, y, 323, 492)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 6);
                            return;
                        }
                        if (isInRange(x, y, 483, 491)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 7);
                            return;
                        }
                        if (isInRange(x, y, 640, 493)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 8);
                            return;
                        }
                        if (isInRange(x, y, 808, 493)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 9);
                            return;
                        }
                        if (isInRange(x, y, 973, 494)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 10);
                            return;
                        }
                        if (isInRange(x, y, 1132, 408)) {
                            gui.sendToIsland(characterCard.getStudents().get(j), 11);
                            return;
                        }
                    }
                }
            };
        };*/
    }

    private boolean isInRange(double x, double y, int startX, int startY) {
        return x > startX && x < (startX + 150) && y > startY && y < (startY + 150);
    }
}
