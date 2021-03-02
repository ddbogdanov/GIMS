package com.cougarneticit.gims.controller;

import com.cougarneticit.gims.application.ResizeHelper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
@FxmlView("/HomeController.fxml")
public class HomeController implements Initializable {

    private static final String ACTIVE_BUTTON ="-fx-background-color: #11AB97";
    private static final String INACTIVE_BUTTON ="-fx-background-color: #007B68";
    private final FxWeaver fxWeaver;
    private Stage stage;

    public static HomeSceneController homeSceneController;

    @FXML AnchorPane pane;
    @FXML JFXButton exitButton, minimizeButton, maximizeButton;
    @FXML JFXButton homeTab, employeeTab, customerTab, roomTab, eventTab, settingsTab;
    @FXML BorderPane mainView;

    public HomeController(FxWeaver fxWeaver) { this.fxWeaver = fxWeaver; }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        stage.setTitle("GIMS - Home");
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);

        mainView.setMinSize(1104, 553);
        mainView.setMaxSize(1920, 1080);
        homeTab.setStyle(ACTIVE_BUTTON);

        //TODO: cycle views
        minimizeButton.setOnAction(e -> {
            Stage stage = (Stage)minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        maximizeButton.setOnAction(e -> {
            Stage stage = (Stage)maximizeButton.getScene().getWindow();
            if(stage.isMaximized()) {
                ResizeHelper.disableWindowDrag(false);
                stage.setMaximized(false);
            }
            else {
                ResizeHelper.disableWindowDrag(true);
                stage.setMaximized(true);
            }
        });
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
    }

    public void show() {
        stage.show();
    }
}
