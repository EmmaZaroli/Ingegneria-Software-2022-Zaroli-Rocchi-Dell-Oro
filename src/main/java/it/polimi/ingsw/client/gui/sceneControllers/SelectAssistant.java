package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.gui.AssistantCardEventHandler;
import it.polimi.ingsw.model.AssistantCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.List;

public class SelectAssistant extends Dialog {
    @FXML
    private GridPane assistants;

    public SelectAssistant(Window owner, List<AssistantCard> deck, AssistantCardEventHandler handler) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it.polimi.ingsw.client.gui/markups/select-assistant.fxml"));
        loader.setController(this);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        setTitle("Play an assistant card");
        try {
            DialogPane pane = loader.load();
            setDialogPane(pane);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }

        int row = 0;
        int column = 0;

        assistants.setVgap(12);
        assistants.setHgap(24);

        for (int i = 0; i < deck.size(); i++) {
            final int j = i;
            Image image = new Image("/it.polimi.ingsw.client.gui/assets/Assistente (" + (deck.get(i).value()) + ").png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(190);
            imageView.setFitWidth(123);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    handler.onSelect(j);
                    getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    close();
                }
            });
            assistants.add(imageView, column, row);
            column++;

            if (column == deck.size() / 2) {
                column = 0;
                row++;
            }
        }
    }
}
