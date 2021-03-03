package com.cougarneticit.gims.controller;

import com.cougarneticit.gims.application.ResizeHelper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
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
@FxmlView("/HomeController.fxml")
public class HomeController implements Initializable {

    //1) "Class" level objects
    //ie. final vars, static objects, etc.
    //button styles
    private static final String ACTIVE_BUTTON ="-fx-background-color: #11AB97";
    private static final String INACTIVE_BUTTON ="-fx-background-color: #007B68";

    //2) static objects, nodes are scenes to be preloaded for each "tab"
    public static Node homeScene, employeeScene, customerScene, roomScene, eventScene, settingsScene;
    public static HomeSceneController homeSceneController;

    //fxWeaver necessities
    private final FxWeaver fxWeaver;
    private Stage stage;

    //3) Springboot
    //ie. Autowire annotations (if necessary)

    //4) FXML annotations
    @FXML AnchorPane pane;
    @FXML JFXButton exitButton, minimizeButton, maximizeButton;
    @FXML JFXButton homeTab, employeeTab, customerTab, roomTab, eventTab, settingsTab;
    @FXML BorderPane mainView;

    public HomeController(FxWeaver fxWeaver) { this.fxWeaver = fxWeaver; }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize the stage, set the scene, apply styling etc.
        this.stage = new Stage();
        stage.setTitle("GIMS - Home");
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);

        preloadViews();

        mainView.setMinSize(1104, 553);
        mainView.setMaxSize(1920, 1080);
        homeTab.setStyle(ACTIVE_BUTTON);

        //Event listeners and action handlers
        homeTab.setOnAction(e -> {
            setActiveButton("home");
            showHomeView();
        });
        employeeTab.setOnAction(e -> {
            setActiveButton("employee");
            showEmployeeView();
        });
        customerTab.setOnAction(e -> {
            setActiveButton("customer");
            showCustomerView();
        });
        roomTab.setOnAction(e -> {
            setActiveButton("room");
            showRoomView();
        });
        eventTab.setOnAction(e -> {
            setActiveButton("event");
           showEventView();
        });
        settingsTab.setOnAction(e -> {
            setActiveButton("settings");
            showSettingsView();
        });

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
        //Gets root nodes for each scene
        homeSceneController = fxWeaver.loadController(HomeSceneController.class); //not sure if necessary. maybe just use user fxWeaver.loadView() instead. having a controller instance might be handy though
        homeScene = homeSceneController.getScene();
    }

    public void show() {
        stage.show();
    }
    public void showHomeView() {

    }
    public void showEmployeeView() {

    }
    public void showCustomerView() {

    }
    public void showRoomView() {

    }
    public void showEventView() {

    }
    public void showSettingsView() {

    }

    //theres probably a better way to do this - store buttons in an array and loop through? or abstract and enumerate all possible values. less lines of code + safer. maybe. too lazy rn
    private void setActiveButton(String activeButtonToSet) {
        switch(activeButtonToSet) {
            case "home":
                homeTab.setStyle(ACTIVE_BUTTON);
                employeeTab.setStyle(INACTIVE_BUTTON);
                customerTab.setStyle(INACTIVE_BUTTON);
                roomTab.setStyle(INACTIVE_BUTTON);
                eventTab.setStyle(INACTIVE_BUTTON);
                settingsTab.setStyle(INACTIVE_BUTTON);
                break;
            case "employee":
                homeTab.setStyle(INACTIVE_BUTTON);
                employeeTab.setStyle(ACTIVE_BUTTON);
                customerTab.setStyle(INACTIVE_BUTTON);
                roomTab.setStyle(INACTIVE_BUTTON);
                eventTab.setStyle(INACTIVE_BUTTON);
                settingsTab.setStyle(INACTIVE_BUTTON);
                break;
            case "customer":
                homeTab.setStyle(INACTIVE_BUTTON);
                employeeTab.setStyle(INACTIVE_BUTTON);
                customerTab.setStyle(ACTIVE_BUTTON);
                roomTab.setStyle(INACTIVE_BUTTON);
                eventTab.setStyle(INACTIVE_BUTTON);
                settingsTab.setStyle(INACTIVE_BUTTON);
                break;
            case "room":
                homeTab.setStyle(INACTIVE_BUTTON);
                employeeTab.setStyle(INACTIVE_BUTTON);
                customerTab.setStyle(INACTIVE_BUTTON);
                roomTab.setStyle(ACTIVE_BUTTON);
                eventTab.setStyle(INACTIVE_BUTTON);
                settingsTab.setStyle(INACTIVE_BUTTON);
                break;
            case "event":
                homeTab.setStyle(INACTIVE_BUTTON);
                employeeTab.setStyle(INACTIVE_BUTTON);
                customerTab.setStyle(INACTIVE_BUTTON);
                roomTab.setStyle(INACTIVE_BUTTON);
                eventTab.setStyle(ACTIVE_BUTTON);
                settingsTab.setStyle(INACTIVE_BUTTON);
                break;
            case "settings":
                homeTab.setStyle(INACTIVE_BUTTON);
                employeeTab.setStyle(INACTIVE_BUTTON);
                customerTab.setStyle(INACTIVE_BUTTON);
                roomTab.setStyle(INACTIVE_BUTTON);
                eventTab.setStyle(INACTIVE_BUTTON);
                settingsTab.setStyle(ACTIVE_BUTTON);
                break;
            default:
                System.err.println("Didn't set button style for some reason");
                break;
        }
    }
}
