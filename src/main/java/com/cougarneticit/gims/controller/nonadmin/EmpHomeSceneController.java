package com.cougarneticit.gims.controller.nonadmin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Reminder;
import com.cougarneticit.gims.model.Task;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.ReminderRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
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

@Component
@FxmlView("/EmpHomeSceneController.fxml")
public class EmpHomeSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    ReminderRepo reminderRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Reminder> reminderListView;
    @FXML private JFXTextArea reminderTextArea;
    @FXML private Label activeTasksLabel, completedTasksLabel;
    @FXML private JFXButton reminderDeleteButton, reminderFormSubmitButton, reminderFormCancelButton;

    public EmpHomeSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        populateReminderListView();
        setInfoLabels();

        reminderFormSubmitButton.setOnAction(e -> {
            submitReminder();
        });
        reminderFormCancelButton.setOnAction(e -> {
            resetReminderForm();
        });
        reminderDeleteButton.setOnAction(e -> {
            deleteReminder();
        });
    }

    //Button Actions - Reminder Form
    private void submitReminder() {
        if(validateReminderForm()) {
            Reminder reminder = new Reminder(
                    getActiveUser(),
                    reminderTextArea.getText());
            reminderRepo.save(reminder);

            populateReminderListView();
            resetReminderForm();
        }
    }
    private void resetReminderForm() {
        reminderTextArea.clear();
    }
    private void deleteReminder() {
        try {
            reminderRepo.delete(reminderListView.getSelectionModel().getSelectedItem());
            populateReminderListView();
        }
        catch(Exception ex) {
            System.err.println("Nothing selected in reminder list");
        }
    }
    //Util Methods - Reminder Form
    private boolean validateReminderForm() {
        return !reminderTextArea.getText().isBlank();
    }
    private void populateReminderListView() {
        ObservableList<Reminder> reminderList = FXCollections.observableArrayList();

        if(reminderRepo.count() != 0){
            reminderList.addAll(reminderRepo.findAllByUser_UserId(getActiveUser().getUserId()));
        }
        reminderListView.setItems(reminderList.sorted());
    }

    //Util Methods - Misc
    private void setInfoLabels() {
        Employee activeEmployee = employeeRepo.findByUser_UserId(getActiveUser().getUserId());

        try {
            activeTasksLabel.setText(String.valueOf(taskRepo.countAllByEmployee_EmployeeIdAndCompleted(activeEmployee.getEmployeeId(), false)));
            completedTasksLabel.setText(String.valueOf(taskRepo.countAllByEmployee_EmployeeIdAndCompleted(activeEmployee.getEmployeeId(), true)));
        }
        catch(NullPointerException ex) {

        }
    }

    public AnchorPane getScene() {
        return pane;
    }
}
