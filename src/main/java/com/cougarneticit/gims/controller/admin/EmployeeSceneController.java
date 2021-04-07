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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

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
    @FXML private Label nameHelpLabel, phoneHelpLabel, emailHelpLabel; //TODO: Hide labels on unfocus also just make form look better
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
            Employee emp = new Employee(UUID.randomUUID(), userComboBox.getSelectionModel().getSelectedItem(), nameTextField.getText(), phoneTextField.getText(), emailTextField.getText());
            employeeRepo.save(emp);
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

    public AnchorPane getScene() { return pane; }
}
