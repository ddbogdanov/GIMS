package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.User;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.UserRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("/EmployeeSceneController.fxml")
public class EmployeeSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<User> userComboBox;
    @FXML private JFXTextField nameTextField, phoneTextField, emailTextField;
    @FXML private Label nameHelpLabel, phoneHelpLabel, emailHelpLabel;
    @FXML private JFXButton addEmployeeButton;

    public EmployeeSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateComboBox();

        addEmployeeButton.setOnAction(e -> {
            //TODO: Sanitize input + error handling
            User activeUser = userComboBox.getSelectionModel().getSelectedItem();
            String name = nameTextField.getText();
            String phone = phoneTextField.getText();
            String email = emailTextField.getText();

            boolean isNameValid = validateName(name);
            boolean isPhoneValid = validatePhone(phone);
            boolean isEmailValid = validateEmail(email);

            if (isNameValid && isPhoneValid && isEmailValid) {
                Employee emp = new Employee(UUID.randomUUID(), activeUser, name, phone, email);
                employeeRepo.save(emp);
            }
            else {
                //TODO: Change color + revert to original text once fixed.
                if(!isNameValid) {
                    nameHelpLabel.setText("Invalid format: First Last");
                }
                else if(!isPhoneValid) {
                    phoneHelpLabel.setText("Invalid format: xxx-xxx-xxxx");
                }
                else {
                    emailHelpLabel.setText("Invalid format: email@domain.com");
                }
            }
        });

        //Focus listeners for text fields. Show/Hide on focus/un-focus
        nameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> nameHelpLabel.setVisible(newVal));
        phoneTextField.focusedProperty().addListener((obs, oldVal, newVal) -> phoneHelpLabel.setVisible(newVal));
        emailTextField.focusedProperty().addListener((obs, oldVal, newVal) -> emailHelpLabel.setVisible(newVal));
    }

    private void populateComboBox() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        if(userRepo.count() != 0) {
            userList.addAll(userRepo.findAll());
        }
        userComboBox.getItems().addAll(userList);
    }
    private boolean validateEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        boolean isValid = emailValidator.isValid(email);
        return isValid;
    }
    private boolean validateName(String name) {
        //TODO
        return true;
    }
    private boolean validatePhone(String phone) {
        //TODO
        Pattern pattern = Pattern.compile("^(\\d{3}[-]?){2}\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public AnchorPane getScene() { return pane; }
}
