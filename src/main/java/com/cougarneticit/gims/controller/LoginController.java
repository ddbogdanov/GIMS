package com.cougarneticit.gims.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/LoginController.fxml")
public class LoginController implements Initializable {

    private final FxWeaver fxWeaver;
    //private static User user; //TODO: IMPLEMENT

    public static HomeController homeController;

    //@Autowired
    //private UserRepo userRepo; //TODO: IMPLEMENT

    @FXML private JFXButton minimizeButton, exitButton, addNewUserButton, databaseButton, loginButton;

    public LoginController(FxWeaver fxWeaver) { this.fxWeaver = fxWeaver; }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        databaseButton.setDisable(true);
        //loginStatus.setVisible(false);

        loginButton.setOnAction(e -> {
            login();
        });
        addNewUserButton.setOnAction(e -> {
            addUser();
        });

        databaseButton.setOnAction(e -> {
            showDbInfo();
        });
        minimizeButton.setOnAction(e -> {
            Stage stage = (Stage)minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
    }

    private void login() {
        homeController = fxWeaver.loadController(HomeController.class);
        homeController.show();
        loginButton.getScene().getWindow().hide();
    }
    private void showDbInfo() {
        //TODO
    }
    private void addUser() {
        //TODO
    }

    //TODO: IMPLEMENT
    /*public static User getUser() {
        return user;
    }*/
}