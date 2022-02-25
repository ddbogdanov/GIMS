package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Reminder;
import com.cougarneticit.gims.model.repos.ReminderRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
@FxmlView("/HomeSceneController.fxml")
public class HomeSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    ReminderRepo reminderRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    TaskRepo taskRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Reminder> reminderListView;
    @FXML private JFXTextArea reminderTextArea;
    @FXML private Label vacantRoomsLabel, occupiedRoomsLabel, activeTasksLabel;
    @FXML private JFXButton reminderDeleteButton, reminderFormSubmitButton, reminderFormCancelButton;

    public HomeSceneController(FxWeaver fxWeaver) {
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

    private void setInfoLabels() {
        int vacantRooms = roomRepo.countAllByRoomStatus_RoomStatus("VACANT");
        int occupiedRooms = roomRepo.countAllByRoomStatus_RoomStatus("OCCUPIED");
        int activeTasks = taskRepo.countAllByCompleted(false);

        vacantRoomsLabel.setText(String.valueOf(vacantRooms));
        occupiedRoomsLabel.setText(String.valueOf(occupiedRooms));
        activeTasksLabel.setText(String.valueOf(activeTasks));
    }

    public AnchorPane getScene() { return pane; }
}
