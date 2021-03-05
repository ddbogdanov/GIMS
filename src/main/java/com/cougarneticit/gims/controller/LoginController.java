package com.cougarneticit.gims.controller;

import com.cougarneticit.gims.model.User;
import com.cougarneticit.gims.model.repos.UserRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

@Component
@FxmlView("/LoginController.fxml")
public class LoginController implements Initializable {

    private final FxWeaver fxWeaver;
    private static User user; //pass logged in user in static context. probably not best way to do this but i dont feel like abstracting stuff

    public static HomeController homeController;

    @Autowired
    private UserRepo userRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton minimizeButton, exitButton, addNewUserButton, databaseButton, loginButton;
    @FXML private Label loginStatus;

    public LoginController(FxWeaver fxWeaver) { this.fxWeaver = fxWeaver; }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        loginStatus.setVisible(false);

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

    //TODO: Differentiate between employee/manager and load appropriate view in home view
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UUID userId;
        boolean isAdmin;

        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

        try {
            if(passwordEncoder.matches(password, userRepo.findByUsername(username).get(0).getPassword())) {

                String hashedPassword = userRepo.findByUsername(username).get(0).getPassword();
                userId = userRepo.findByUsername(username).get(0).getId();
                isAdmin = userRepo.findByUsername(username).get(0).getIsAdmin();
                user = new User(userId, username, hashedPassword, isAdmin);

                loginStatus.setTextFill(Color.web("#FFFFFF"));
                loginStatus.setText("Login Successful!");
                loginStatus.setVisible(true);
                homeController = fxWeaver.loadController(HomeController.class);
                homeController.show();
                loginButton.getScene().getWindow().hide();

            } else {
                loginStatus.setTextFill(Color.web("#F73331"));
                loginStatus.setVisible(true);
                loginStatus.setText("Password is incorrect");
            }
        }
        catch(IndexOutOfBoundsException ex) {
            loginStatus.setTextFill(Color.web("#F73331"));
            loginStatus.setVisible(true);
            loginStatus.setText("This username may not exist");
        }
    }
    private void showDbInfo() {
        fxWeaver.loadController(DatabaseInfoController.class).show();
    }
    private void addUser() {
        fxWeaver.loadController(AddUserController.class).show();
    }
    public static User getUser() {
        return user;
    }
    public AnchorPane getScene() {
        return pane;
    }
}