package com.cougarneticit.gims.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/RoomsSceneController.fxml")
public class RoomsSceneController implements Initializable {

    private final FxWeaver fxWeaver;
    private Stage stage;

    @FXML private AnchorPane pane;

    public RoomsSceneController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
    }

    public AnchorPane getScene() {
        return pane;
    }
}