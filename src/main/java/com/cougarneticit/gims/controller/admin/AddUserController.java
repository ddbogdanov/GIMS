package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.model.User;
import com.cougarneticit.gims.model.repos.UserRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

@Component
@FxmlView("/AddUserController.fxml")
public class AddUserController implements Initializable {

    private FxWeaver fxWeaver;
    private Stage stage;

    @Autowired
    private UserRepo userRepo;

    @FXML AnchorPane pane;
    @FXML JFXButton submitButton, closeButton;
    @FXML JFXTextField usernameTextField;
    @FXML JFXPasswordField userPasswordField, userConfirmPasswordField;
    @FXML JFXCheckBox isAdminCheck, removeUserCheck;
    @FXML Label passMatchField, userTitle;

    public AddUserController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);

        passMatchField.setVisible(false);

        removeUserCheck.setOnAction(e -> {
            if(removeUserCheck.isSelected()){
                userTitle.setText("Remove a User");
                isAdminCheck.setDisable(true);
            }
            else {
                userTitle.setText("Add a New User");
                isAdminCheck.setDisable(false);
            }
        });
        submitButton.setOnAction(e -> {
            submit();
        });
        closeButton.setOnAction(e -> {
            Stage stage = (Stage)closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void submit() {
        String username = usernameTextField.getText();
        String password = userPasswordField.getText();
        String confirmPassword = userConfirmPasswordField.getText();
        String fetchedPassword;
        boolean isAdmin = isAdminCheck.isSelected();
        boolean shouldRemove = removeUserCheck.isSelected();

        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        if(!shouldRemove) {
            if(!username.isBlank() && !password.isBlank()) {
                try {
                    userRepo.findByUsername(username).get(0); //If returned array is empty the username does not exist in database - Exception is thrown
                    passMatchField.setTextFill(Color.web("#F73331"));
                    passMatchField.setText("This username already exists");
                    passMatchField.setVisible(true);
                } catch (IndexOutOfBoundsException ex) { //Catch exception and add new user
                    if (password.equals(confirmPassword)) {
                        User user = new User(UUID.randomUUID(), username, hashedPassword, isAdmin);
                        userRepo.save(user);
                        passMatchField.setTextFill(Color.web("#FFFFFF"));
                        passMatchField.setText("New user added!");
                    } else {
                        passMatchField.setTextFill(Color.web("#F73331"));
                        passMatchField.setText("The passwords do not match");
                    }
                    passMatchField.setVisible(true);
                }
            }
            else {
                passMatchField.setTextFill(Color.web("#F73331"));
                passMatchField.setText("Username and/or password cannot be blank!");
                passMatchField.setVisible(true);
            }
        }
        else {
            try {
                fetchedPassword = userRepo.findByUsername(username).get(0).getPassword();
                if (password.equals(confirmPassword) && passwordEncoder.matches(password, fetchedPassword)) {
                    userRepo.deleteByUsername(username);
                    passMatchField.setTextFill(Color.web("#FFFFFF"));
                    passMatchField.setText("User deleted!");
                } else {
                    passMatchField.setTextFill(Color.web("#F73331"));
                    passMatchField.setText("The passwords do not match");
                }
                passMatchField.setVisible(true);
            } catch (IndexOutOfBoundsException ex) {
                passMatchField.setTextFill(Color.web("#F73331"));
                passMatchField.setText("This username does not exist");
                passMatchField.setVisible(true);
            }
        }
    }

    public void show() {
        stage.show();
    }
}