package com.cougarneticit.gims.controller.nonadmin;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.controller.admin.HomeSceneController;
import com.cougarneticit.gims.controller.common.GIMSController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/EmpHomeController.fxml")
public class EmpHomeController extends GIMSController implements Initializable {

    public static Node empHomeScene;
    public static EmpHomeSceneController empHomeSceneController;

    private Stage stage;

    @FXML private AnchorPane pane;
    @FXML BorderPane mainView;

    public EmpHomeController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        stage.setTitle("GIMS - Home");
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);

        preloadViews();

        mainView.setCenter(empHomeScene);
    }

    public void preloadViews() {
        empHomeSceneController = fxWeaver.loadController(EmpHomeSceneController.class);
        empHomeScene = empHomeSceneController.getScene();
    }
    public void show() {
        stage.show();
    }
}
