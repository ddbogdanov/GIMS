package com.cougarneticit.gims.controller.nonadmin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.Task;
import com.cougarneticit.gims.model.repos.*;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@Component
@FxmlView("/EmpRoomsSceneController.fxml")
public class EmpRoomsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    TaskRepo taskRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    PriorityRepo priorityRepo;
    @Autowired
    RoomStatusRepo roomStatusRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Room> roomListView;
    @FXML private JFXListView<Task> taskListView;
    @FXML private JFXTextField taskNameTextField;
    @FXML private JFXTextArea taskDescriptionTextArea;
    @FXML private Label roomNameLabel, activeTasksLabel, statusLabel, priorityLabel, dueDateLabel, completedLabel;
    @FXML private JFXButton setCompletedButton;

    public EmpRoomsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        try {
            populateRoomListView();
            roomListView.getSelectionModel().select(0);
            setInfoLabels();
            populateTaskListView(roomListView.getSelectionModel().getSelectedItem().getRoomId());
            taskListView.getSelectionModel().select(0);
            setTaskInfoLabels();
        }
        catch(NullPointerException ex) {
            System.err.println("No rooms / or no tasks for selected room");
        }

        setCompletedButton.setOnAction(e -> {
            setTaskAsCompleted();
        });

        roomListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                roomListView.getSelectionModel().select(oldVal);
            }
            else {
                setInfoLabels();
                populateTaskListView(newVal.getRoomId());
            }
        });
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                resetTaskInfo();
            }
            else {
                setTaskInfoLabels();
            }
        });
    }

    private void setTaskAsCompleted() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();

        boolean newCompletedValue = !selectedTask.isCompleted();
        selectedTask.setCompleted(newCompletedValue);

        taskRepo.save(selectedTask);

        int oldIndex = taskListView.getSelectionModel().getSelectedIndex();
        populateTaskListView(roomListView.getSelectionModel().getSelectedItem().getRoomId());
        taskListView.getSelectionModel().select(oldIndex);
        setInfoLabels();
        setTaskInfoLabels();
    }
    private void populateRoomListView() {
        ObservableList<Room> roomList = FXCollections.observableArrayList();

        if(roomRepo.count() != 0) {
            roomList.addAll(roomRepo.findAll());
        }
        roomListView.setItems(roomList.sorted());
    }
    private void populateTaskListView(char roomId) {
        ObservableList<Task> taskList = FXCollections.observableArrayList();

        if(taskRepo.count() != 0){
            taskList.addAll(taskRepo.findAllByRoom_RoomId(roomId));
        }

        taskListView.setItems(taskList);
    }
    private void setInfoLabels() {
        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();

        roomNameLabel.setText(selectedRoom.getRoomId() + ": " + selectedRoom.getRoomName());
        activeTasksLabel.setText(String.valueOf(taskRepo.countAllByRoom_RoomIdAndCompleted(selectedRoom.getRoomId(), false)));
        statusLabel.setText(String.valueOf(selectedRoom.getRoomStatus()));
        switch(selectedRoom.getRoomStatus().getEventStatus()) {
            case "OCCUPIED":
                statusLabel.setTextFill(Color.web("#F73331"));
                break;
            case "SERVICE":
                statusLabel.setTextFill(Color.web("#F7F74A"));
                break;
            case "VACANT":
                statusLabel.setTextFill(Color.web("#58AB33"));
                break;
            default:
                statusLabel.setTextFill(Color.web("#5D5C67"));
        }
    }
    private void setTaskInfoLabels() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if(selectedTask.isCompleted()) {
            completedLabel.setTextFill(Color.web("#58AB33"));
            completedLabel.setText("Yes");
        }
        else {
            completedLabel.setTextFill(Color.web("#F73331"));
            completedLabel.setText("No");
        }
        switch(selectedTask.getPriority().toString()) {
            case "HIGH":
                priorityLabel.setTextFill(Color.web("#F73331"));
                break;
            case "MEDIUM":
                priorityLabel.setTextFill(Color.web("#F7F74A"));
                break;
            case "LOW":
                priorityLabel.setTextFill(Color.web("#58AB33"));
                break;
            default:
                priorityLabel.setTextFill(Color.web("#5D5C67"));
        }

        taskNameTextField.setText(selectedTask.getName());
        taskDescriptionTextArea.setText(selectedTask.getDescription());
        priorityLabel.setText(selectedTask.getPriority().toString());
        dueDateLabel.setText(selectedTask.getDueDate());

        priorityLabel.setVisible(true);
        dueDateLabel.setVisible(true);
        completedLabel.setVisible(true);
    }
    private void resetTaskInfo() {
        taskNameTextField.clear();
        priorityLabel.setVisible(false);
        dueDateLabel.setVisible(false);
        completedLabel.setVisible(false);
        taskDescriptionTextArea.clear();
    }

    public AnchorPane getScene() {
        return pane;
    }
}