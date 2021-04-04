package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.controller.common.GIMSController;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/HomeController.fxml")
public class HomeController extends GIMSController implements Initializable {
    //TODO: Implement mutlithreading. UI on main, non-UI functions on secondary
    //1) "Class" level objects
    //ie. final vars, static objects, etc.

    //2) static objects, nodes are scenes to be preloaded for each "tab" - should get rid of statics eventually though
    public static Node homeScene, employeeScene, customersScene, roomsScene, eventsScene, settingsScene;
    public static HomeSceneController homeSceneController;
    public static EmployeeSceneController employeeSceneController;
    public static CustomersSceneController customersSceneController;
    public static RoomsSceneController roomsSceneController;
    public static EventsSceneController eventsSceneController;
    public static SettingsSceneController settingsSceneController;

    //fxWeaver necessities
    //private final FxWeaver fxWeaver;
    private Stage stage;

    //3) Springboot
    //ie. Autowire annotations (if necessary)

    //4) FXML annotations
    @FXML AnchorPane pane;
    @FXML JFXButton exitButton, minimizeButton, maximizeButton;
    @FXML JFXButton homeTab, employeeTab, customerTab, roomTab, eventTab, settingsTab;
    @FXML BorderPane mainView;

    public HomeController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize the stage, set the scene, apply styling etc.
        this.stage = new Stage();
        initStage(stage, pane,"GIMS - Home/Admin", StageStyle.UNDECORATED, true);

        ArrayList<JFXButton> buttonList = new ArrayList<>();
        buttonList.add(homeTab);
        buttonList.add(employeeTab);
        buttonList.add(customerTab);
        buttonList.add(roomTab);
        buttonList.add(eventTab);
        buttonList.add(settingsTab);

        preloadViews();

        /*
        mainView.setMinSize(1104, 553);
        mainView.setMaxSize(1920, 1080);
        * TODO: need to set min and max size of entire window. too lazy rn
        */

        setActiveButton(homeTab, buttonList);
        showHomeView();

        //Event listeners and action handlers
        homeTab.setOnAction(e -> {
            setActiveButton(homeTab, buttonList);
            showHomeView();
        });
        employeeTab.setOnAction(e -> {
            setActiveButton(employeeTab, buttonList);
            showEmployeeView();
        });
        customerTab.setOnAction(e -> {
            setActiveButton(customerTab, buttonList);
            showCustomersView();
        });
        roomTab.setOnAction(e -> {
            setActiveButton(roomTab, buttonList);
            showRoomsView();
        });
        eventTab.setOnAction(e -> {
            setActiveButton(eventTab, buttonList);
            showEventsView();
        });
        settingsTab.setOnAction(e -> {
            setActiveButton(settingsTab, buttonList);
            showSettingsView();
        });

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
        //First loads controller class
        //Then gets root nodes for each scene
        homeSceneController = fxWeaver.loadController(HomeSceneController.class); //not sure if necessary. maybe just use user fxWeaver.loadView() instead. having a controller instance might be handy though
        homeScene = homeSceneController.getScene();

        employeeSceneController = fxWeaver.loadController(EmployeeSceneController.class);
        employeeScene = employeeSceneController.getScene();

        customersSceneController = fxWeaver.loadController(CustomersSceneController.class);
        customersScene = customersSceneController.getScene();

        roomsSceneController = fxWeaver.loadController(RoomsSceneController.class);
        roomsScene = roomsSceneController.getScene();

        eventsSceneController = fxWeaver.loadController(EventsSceneController.class);
        eventsScene = eventsSceneController.getScene();

        settingsSceneController = fxWeaver.loadController(SettingsSceneController.class);
        settingsScene = settingsSceneController.getScene();
    }

    public void show() {
        stage.show();
    }
    public void showHomeView() {
        mainView.setCenter(homeScene);
    }
    public void showEmployeeView() {
        mainView.setCenter(employeeScene);
    }
    public void showCustomersView() {
        mainView.setCenter(customersScene);
    }
    public void showRoomsView() {
        mainView.setCenter(roomsScene);
    }
    public void showEventsView() {
        mainView.setCenter(eventsScene);
    }
    public void showSettingsView() {
        mainView.setCenter(settingsScene);
    }

}
