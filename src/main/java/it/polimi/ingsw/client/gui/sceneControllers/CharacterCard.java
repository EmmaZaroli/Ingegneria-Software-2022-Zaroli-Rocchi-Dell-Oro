package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A class representing a Caracter card
 */
public class CharacterCard extends Pane {
    private ViewCharacterCard characterCard;

    private Gui gui;

    @FXML
    private int index;

    private boolean isDragAndDropEnabled = false;
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

    @FXML
    private ImageView imageView;
    @FXML
    private ImageView coin;
    @FXML
    private GridPane students;

    /**
     * Creates a card
     *
     * @param index the index of the card in the deck
     */
    public CharacterCard(@NamedArg("index") int index) {
        this.index = index;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/character-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Sets a reference to the main application controller
     *
     * @param gui A reference to the main application controller
     */
    public void setController(Gui gui) {
        this.gui = gui;
    }

    /**
     * Selects the image to load based on the card
     *
     * @param card The character to use when loading the image
     */
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

            imageView.setOnMouseClicked(mouseEvent -> {
                if (gui.canActivateCharacterProxy(card)) {
                    switch (characterCard.getCharacter()) {
                        case CHARACTER_ONE -> {
                            gui.genericMessage("You activated the Character.\nMove a student from the CharacterCard to an island");
                            isDragAndDropEnabled = true;
                        }
                        case CHARACTER_TWO, CHARACTER_SIX, CHARACTER_EIGHT -> {
                            gui.genericMessage("You activated the Character.");
                            gui.activateCharacter(index);
                        }
                        case CHARACTER_FOUR -> {
                            gui.setAdditionalMotherNatureMovement(2);
                            gui.genericMessage("You activated the Character");
                            gui.activateCharacter(index);
                        }
                        case CHARACTER_SEVEN -> handleEffectSeven();
                        case CHARACTER_NINE -> handleEffectNine();
                        case CHARACTER_ELEVEN -> handleEffectEleven();
                    }
                }
            });

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

            imageView.setOnMouseReleased(getReleasedCallback(card.getCharacter(), i, card.getStudents().get(i)));
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

    /**
     * handles the effect card nine
     */
    private void handleEffectNine() {
        Platform.runLater(() -> {
            List<ButtonType> buttons = new LinkedList<>();
            buttons.add(new ButtonType("Red", ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType("Green", ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType("Blue", ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType("Pink", ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType("Yellow", ButtonBar.ButtonData.OK_DONE));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chose a color to invalidate", buttons.toArray(new ButtonType[]{}));
            Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent()) {
                PawnColor color = switch (response.get().getText()) {
                    case "Red" -> PawnColor.RED;
                    case "Green" -> PawnColor.GREEN;
                    case "Blue" -> PawnColor.BLUE;
                    case "Pink" -> PawnColor.PINK;
                    case "Yellow" -> PawnColor.YELLOW;
                    default -> PawnColor.NONE;
                };
                if (!gui.activateCharacter(index, color)) {
                    gui.error("Error while activating the effect");
                }
            } else {
                handleEffectNine();
            }
        });
    }

    /**
     * Returns a string with the name of the given color
     *
     * @param color The color to translate
     * @return A string representing the color
     */
    private String resolveColorString(PawnColor color) {
        return switch (color) {
            case RED -> "Red";
            case GREEN -> "Green";
            case BLUE -> "Blue";
            case YELLOW -> "Yellow";
            case PINK -> "Pink";
            case NONE -> "None";
        };
    }

    /**
     * Handles the effect linked to the character 11
     */
    private void handleEffectEleven() {
        Platform.runLater(() -> {
            List<ButtonType> buttons = new LinkedList<>();
            buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(0)), ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(1)), ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(2)), ButtonBar.ButtonData.OK_DONE));
            buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(3)), ButtonBar.ButtonData.OK_DONE));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chose a student to place in your dinning room", buttons.toArray(new ButtonType[]{}));
            Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent()) {
                PawnColor color = switch (response.get().getText()) {
                    case "Red" -> PawnColor.RED;
                    case "Green" -> PawnColor.GREEN;
                    case "Blue" -> PawnColor.BLUE;
                    case "Pink" -> PawnColor.PINK;
                    case "Yellow" -> PawnColor.YELLOW;
                    default -> PawnColor.NONE;
                };
                if (!gui.activateCharacter(index, color)) {
                    gui.error("Error while activating the effect");
                }
            } else {
                handleEffectEleven();
            }
        });
    }

    /**
     * Returns an eventHandler for the card ant position j
     *
     * @param character The character of the given card
     * @param j         The index of the card in the deck
     * @param color     The color of the selected pawn
     * @return The eventHandler for the card
     */
    private EventHandler<MouseEvent> getReleasedCallback(Character character, int j, PawnColor color) {
        return switch (character) {
            case CHARACTER_ONE -> new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isDragAndDropEnabled) {
                        double x = mouseEvent.getScreenX();
                        double y = mouseEvent.getScreenY();
                        isDragAndDropEnabled = false;
                        if (isInRange(x, y, 158, 404)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(0).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 324, 317)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(1).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 482, 316)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(2).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 648, 319)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(3).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 814, 318)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(4).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 971, 324)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(5).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 323, 492)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(6).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 483, 491)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(7).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 640, 493)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(8).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 808, 493)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(9).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 973, 494)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(10).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        if (isInRange(x, y, 1132, 408)) {
                            if (!gui.activateCharacter(index, color, gui.getIslands().get(11).getIsland().getUuid())) {
                                gui.error("Error while activating the effect");
                            }
                            return;
                        }
                        isDragAndDropEnabled = true;
                        gui.error("Invalid selection");
                    }
                }
            };
            default -> new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            };
        };
    }

    /**
     * Handles the effect of the character 7
     */
    private void handleEffectSeven() {
        int i = 0;
        boolean shouldContinue = true;
        while (i < 3 && shouldContinue) {
            Platform.runLater(() -> {
                List<ButtonType> buttons = new LinkedList<>();
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(0)), ButtonBar.ButtonData.OK_DONE));
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(1)), ButtonBar.ButtonData.OK_DONE));
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(2)), ButtonBar.ButtonData.OK_DONE));
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(3)), ButtonBar.ButtonData.OK_DONE));
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(4)), ButtonBar.ButtonData.OK_DONE));
                buttons.add(new ButtonType(resolveColorString(characterCard.getStudents().get(5)), ButtonBar.ButtonData.OK_DONE));

                buttons.add(new ButtonType("End effect", ButtonBar.ButtonData.CANCEL_CLOSE));

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chose a student to place in your entrance", buttons.toArray(new ButtonType[]{}));
                Optional<ButtonType> response = alert.showAndWait();
                if (response.isPresent()) {
                    if (!response.get().getButtonData().isCancelButton()) {
                        PawnColor fromCard = switch (response.get().getText()) {
                            case "Red" -> PawnColor.RED;
                            case "Green" -> PawnColor.GREEN;
                            case "Blue" -> PawnColor.BLUE;
                            case "Pink" -> PawnColor.PINK;
                            case "Yellow" -> PawnColor.YELLOW;
                            default -> PawnColor.NONE;
                        };

                        List<ButtonType> buttons2 = new LinkedList<>();
                        for (int ii = 0; ii < gui.getMe().getBoard().getEntrance().size(); ii++) {
                            buttons.add(new ButtonType(resolveColorString(gui.getMe().getBoard().getEntrance().get(ii)), ButtonBar.ButtonData.OK_DONE));
                        }

                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Chose a student to remove from your entrance", buttons2.toArray(new ButtonType[]{}));
                        Optional<ButtonType> response2 = alert2.showAndWait();
                        if (response2.isPresent()) {
                            PawnColor fromEntrance = switch (response.get().getText()) {
                                case "Red" -> PawnColor.RED;
                                case "Green" -> PawnColor.GREEN;
                                case "Blue" -> PawnColor.BLUE;
                                case "Pink" -> PawnColor.PINK;
                                case "Yellow" -> PawnColor.YELLOW;
                                default -> PawnColor.NONE;
                            };

                            if (!gui.activateCharacter(index, fromCard, fromEntrance)) {
                                gui.error("Error while activating the effect");
                            }
                        }
                    }
                } else {
                    handleEffectSeven();
                }
            });
        }
    }

    /**
     * Checks if a point x, y is included in the square of edge 150 starting at startX, startY
     *
     * @return true if the point is included
     */
    private boolean isInRange(double x, double y, int startX, int startY) {
        return x > startX && x < (startX + 150) && y > startY && y < (startY + 150);
    }
}
