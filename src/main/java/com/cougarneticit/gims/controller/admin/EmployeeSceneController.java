package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
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
@FxmlView("/EmployeeSceneController.fxml")
public class EmployeeSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @FXML private AnchorPane pane;

    public EmployeeSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
    }

    public AnchorPane getScene() { return pane; }
}
