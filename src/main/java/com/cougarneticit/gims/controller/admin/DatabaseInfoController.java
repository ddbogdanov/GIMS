package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.controller.common.GIMSController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/DatabaseInfoController.fxml")
public class DatabaseInfoController extends GIMSController implements Initializable {

    //TODO: Implement

    private Stage stage;

    @FXML private AnchorPane pane;
    @FXML private JFXButton cancelButton;

    public DatabaseInfoController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, StageStyle.TRANSPARENT, Modality.APPLICATION_MODAL, Color.TRANSPARENT, false);

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage)cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    public AnchorPane getScene() { return pane; }
    public void show() {
        stage.show();
    }
}
