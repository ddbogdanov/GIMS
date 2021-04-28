package com.cougarneticit.gims.controller;

import com.cougarneticit.gims.controller.admin.AddUserController;
import com.cougarneticit.gims.controller.admin.DatabaseInfoController;
import com.cougarneticit.gims.controller.admin.HomeController;
import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.controller.nonadmin.EmpHomeController;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

@Component
@FxmlView("/LoginController.fxml")
public class LoginController extends GIMSController implements Initializable {

    public static HomeController homeController; //TODO remove static references
    public static EmpHomeController empHomeController;

    @Autowired
    private UserRepo userRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton minimizeButton, exitButton, addNewUserButton, databaseButton, loginButton;
    @FXML private Label loginStatus;

    public LoginController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

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

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UUID userId;
        boolean isAdmin;

        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

        try {
            //User user = new User(UUID.randomUUID(), "LeslD", "admin", true); // Debug
            User user = userRepo.findByUsername(username).get(0);
            String userPassword = user.getPassword();
            
            if(passwordEncoder.matches(password, userPassword)) {

<<<<<<< Updated upstream
                String hashedPassword = userRepo.findByUsername(username).get(0).getPassword();
                userId = userRepo.findByUsername(username).get(0).getUserId();
                isAdmin = userRepo.findByUsername(username).get(0).isAdmin();
                User user = new User(userId, username, hashedPassword, isAdmin);
=======
                userId = user.getUser_id();
                isAdmin = user.isAdmin();
                User activeUser = new User(userId, username, userPassword, isAdmin);
>>>>>>> Stashed changes

                loginStatus.setTextFill(Color.web("#FFFFFF"));
                loginStatus.setText("Login Successful!");
                loginStatus.setVisible(true);

                setActiveUser(activeUser);

                if(activeUser.isAdmin()) {
                    loadAdminView();
                }
                else {
                    loadNonAdminView();
                }

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
    private void loadAdminView() {
        HomeController hc = new HomeController(fxWeaver);
        homeController = fxWeaver.loadController(hc.getClass());
        homeController.show();
    }
    private void loadNonAdminView() {
        EmpHomeController ehc = new EmpHomeController((fxWeaver));
        empHomeController = fxWeaver.loadController(ehc.getClass());
        empHomeController.show();
    }

    public AnchorPane getScene() {
        return pane;
    }
}