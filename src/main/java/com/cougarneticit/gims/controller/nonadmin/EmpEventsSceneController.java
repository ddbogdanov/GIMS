package com.cougarneticit.gims.controller.nonadmin;

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
@FxmlView("/EmpEventsSceneController.fxml")
public class EmpEventsSceneController implements Initializable {

    private final FxWeaver fxWeaver;
    private Stage stage;

    @FXML private AnchorPane pane;

    public EmpEventsSceneController(FxWeaver fxWeaver) {
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
