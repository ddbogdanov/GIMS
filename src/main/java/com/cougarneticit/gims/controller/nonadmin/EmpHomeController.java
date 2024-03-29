package com.cougarneticit.gims.controller.nonadmin;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.controller.common.GIMSController;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/EmpHomeController.fxml")
public class EmpHomeController extends GIMSController implements Initializable {

    public static Node empHomeScene, empRoomsScene, empEventsScene, empSettingsScene;
    public static EmpHomeSceneController empHomeSceneController;
    public static EmpRoomsSceneController empRoomsSceneController;
    public static EmpShiftsSceneController empShiftsSceneController;
    public static EmpSettingsSceneController empSettingsSceneController;

    private Stage stage;

    @FXML AnchorPane pane;
    @FXML JFXButton exitButton, minimizeButton, maximizeButton;
    @FXML JFXButton homeTab, roomTab, shiftTab, settingsTab;
    @FXML BorderPane mainView;

    public EmpHomeController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane,"GIMS - Home/Employee", StageStyle.UNDECORATED, null, null, true);

        ArrayList<JFXButton> buttonList = new ArrayList<>();
        buttonList.add(homeTab);
        buttonList.add(roomTab);
        buttonList.add(shiftTab);
        //buttonList.add(settingsTab);

        preloadViews();

        setActiveButton(homeTab, buttonList);
        mainView.setCenter(empHomeScene);

        homeTab.setOnAction(e -> {
            setActiveButton(homeTab, buttonList);
            showEmpHomeView();
        });
        roomTab.setOnAction(e -> {
            setActiveButton(roomTab, buttonList);
            showEmpRoomsView();
        });
        shiftTab.setOnAction(e -> {
            setActiveButton(shiftTab, buttonList);
            showEmpShiftsView();
        });
        /*settingsTab.setOnAction(e -> {
            setActiveButton(settingsTab, buttonList);
            showEmpSettingsView();
        });*/

        //Duplicated code - fix by dynamically loading tabs into a common HomeController. Too much work for me.
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

    public void preloadViews() {
        empHomeSceneController = fxWeaver.loadController(EmpHomeSceneController.class);
        empHomeScene = empHomeSceneController.getScene();

        empRoomsSceneController = fxWeaver.loadController(EmpRoomsSceneController.class);
        empRoomsScene = empRoomsSceneController.getScene();

        empShiftsSceneController = fxWeaver.loadController(EmpShiftsSceneController.class);
        empEventsScene = empShiftsSceneController.getScene();

        empSettingsSceneController = fxWeaver.loadController(EmpSettingsSceneController.class);
        empSettingsScene = empSettingsSceneController.getScene();
    }

    public void show() {
        stage.show();
    }
    public void showEmpHomeView() {
        mainView.setCenter(empHomeScene);
    }
    public void showEmpRoomsView() {
        mainView.setCenter(empRoomsScene);
    }
    public void showEmpShiftsView() {
        mainView.setCenter(empEventsScene);
    }
    public void showEmpSettingsView() {
        mainView.setCenter(empSettingsScene);
    }
}
