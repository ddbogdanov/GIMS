package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.common.Priority;
import com.cougarneticit.gims.model.common.RoomStatus;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
@FxmlView("/RoomsSceneController.fxml")
public class RoomsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    TaskRepo taskRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    EmployeeRepo employeeRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<Priority> priorityComboBox;
    @FXML private JFXComboBox<RoomStatus> roomStatusComboBox;
    @FXML private JFXComboBox<Room> roomComboBox;
    @FXML private JFXComboBox<Employee> employeeComboBox;
    @FXML private JFXListView<Room> roomListView;
    @FXML private JFXToggleButton editToggleButton;
    @FXML private JFXTextField roomIdTextField, roomNameTextField, taskNameTextField;
    @FXML private JFXTextArea taskDescriptionTextArea;
    @FXML private Label roomFormLabel, roomNameLabel, roomStatusHelpLabel, activeTasksLabel, statusLabel, roomIdHelpLabel, roomNameHelpLabel, taskNameHelpLabel, taskDescriptionHelpLabel;
    @FXML private JFXButton roomFormSubmitButton, roomFormCancelButton, roomFormDeleteButton;

    public RoomsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        populateRoomListView();
        roomListView.getSelectionModel().select(0);
        setInfoLabels();

        //Initialize ComboBoxes
        priorityComboBox.getItems().addAll(Priority.values());
        roomStatusComboBox.getItems().addAll(RoomStatus.values());
        roomComboBox.getItems().addAll(roomRepo.findAll());
        roomComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Room room) {
                return "Suite " + room.getRoomId();
            }
            @Override
            public Room fromString(String s) {
                return null;
            }
        });
        employeeComboBox.getItems().addAll(employeeRepo.findAll());

        editToggleButton.setOnAction(e -> {
            editToggle();
        });
        roomFormSubmitButton.setOnAction(e -> {
            submitRoom();
        });
        roomFormCancelButton.setOnAction(e -> {
            resetRoomForm();
        });
        roomFormDeleteButton.setOnAction(e -> {
            deleteRoom();
        });

        //Event Listeners
        roomIdTextField.focusedProperty().addListener((obs, oldVal, newVal) -> roomIdHelpLabel.setVisible(newVal));
        roomNameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> roomNameHelpLabel.setVisible(newVal));
        taskNameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> taskNameHelpLabel.setVisible(newVal));
        taskDescriptionTextArea.focusedProperty().addListener((obs, oldVal, newVal) -> taskDescriptionHelpLabel.setVisible(newVal));
        roomListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                roomListView.getSelectionModel().select(oldVal);
            }
            setInfoLabels();
            if(editToggleButton.isSelected()) {
                populateRoomForm();
            }
        });
        roomIdTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.length() > 1) {
                roomIdTextField.setText(oldVal);
            }
        });
    }

    private void editToggle() {
        if(editToggleButton.isSelected()) {
            try {
                roomFormSubmitButton.setOnAction(e -> {
                    submitRoomEdits();
                });

                populateRoomForm();
                roomIdTextField.setDisable(true);
                roomFormLabel.setText("Edit a Room");
                roomFormSubmitButton.setText("Submit");
            }
            catch(NullPointerException ex) {
                roomFormSubmitButton.setOnAction(ee -> {
                    submitRoom();
                });
                System.err.println("Nothing selected - can't edit");
                //TODO
            }
        }
        else {
            resetRoomForm();
        }
    }
    private void submitRoomEdits() {
        if(validateRoomForm()) {
            Room updatedRoom = new Room(
                    roomIdTextField.getText().charAt(0),
                    roomNameTextField.getText(),
                    roomStatusComboBox.getValue());
            roomRepo.save(updatedRoom);

            populateRoomListView();
            resetRoomForm();
        }
    }
    private void submitRoom() {
        if(validateRoomForm()) {
            if (!roomRepo.existsById(roomIdTextField.getText().charAt(0))) {
                Room room = new Room(roomIdTextField.getText().charAt(0), roomNameTextField.getText(), roomStatusComboBox.getValue());
                roomRepo.save(room);

                populateRoomListView();
                resetRoomForm();
            } else {
                roomIdHelpLabel.setText("An existing room already has this ID");
                roomIdHelpLabel.setTextFill(Color.web("#F73331"));
                roomIdHelpLabel.setVisible(true);
            }
        }
    }
    private void deleteRoom() {
        try {
            Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();
            roomRepo.deleteById(selectedRoom.getRoomId());

            populateRoomListView();
            resetRoomForm();
        }
        catch(Exception ex) {
            System.err.println("Nothing selected in list");
        }
    }

    private void populateRoomListView() {
        ObservableList<Room> roomList = FXCollections.observableArrayList();

        if(roomRepo.count() != 0) {
            roomList.addAll(roomRepo.findAll());
        }
        roomListView.setItems(roomList.sorted());
    }
    private void populateRoomForm() {
        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();

        roomIdTextField.setText(String.valueOf(selectedRoom.getRoomId()));
        roomNameTextField.setText(selectedRoom.getRoomName());
        roomStatusComboBox.getSelectionModel().select(selectedRoom.getStatus());
    }
    private boolean validateRoomForm() {
        if(roomIdTextField.getText().isBlank()) {
            roomIdHelpLabel.setText("ID cannot be blank");
            roomIdHelpLabel.setTextFill(Color.web("#F73331"));
            roomIdHelpLabel.setVisible(true);
            return false;
        }
        else if(roomNameTextField.getText().isBlank()) {
            roomNameHelpLabel.setText("Name cannot be blank");
            roomNameHelpLabel.setTextFill(Color.web("#F73331"));
            roomNameHelpLabel.setVisible(true);
            return false;
        }
        else if(roomStatusComboBox.getSelectionModel().isEmpty()) {
            roomStatusHelpLabel.setText("Status cannot be blank");
            roomStatusHelpLabel.setTextFill(Color.web("#F73331"));
            roomStatusHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }
    }
    private void resetRoomForm() {
        roomStatusComboBox.getSelectionModel().clearSelection();

        editToggleButton.selectedProperty().setValue(false);
        roomFormSubmitButton.setText("Add Room");

        roomFormLabel.setText("Add a Room");

        roomIdTextField.clear();
        roomIdTextField.setDisable(false);
        roomNameTextField.clear();

        roomIdHelpLabel.setText("Single Character");
        roomIdHelpLabel.setTextFill(Color.web("#5BDDC7"));
        roomIdHelpLabel.setVisible(false);
        roomNameHelpLabel.setText("120 Character Max");
        roomNameHelpLabel.setTextFill(Color.web("#5BDDC7"));
        roomNameHelpLabel.setVisible(false);
        roomStatusHelpLabel.setText("Room Status");
        roomStatusHelpLabel.setTextFill(Color.web("#5BDDC7"));
        roomStatusHelpLabel.setVisible(false);

        //Reset event handler
        roomFormSubmitButton.setOnAction(e -> {
            submitRoom();
        });
    }

    private void setInfoLabels() {
        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();

        roomNameLabel.setText(selectedRoom.getRoomId() + ": " + selectedRoom.getRoomName());
        statusLabel.setText(String.valueOf(selectedRoom.getStatus()));
        switch(selectedRoom.getStatus()) {
            case OCCUPIED:
                statusLabel.setTextFill(Color.web("#F73331"));
                break;
            case SERVICE:
                statusLabel.setTextFill(Color.web("#F7F74A"));
                break;
            case VACANT:
                statusLabel.setTextFill(Color.web("#58AB33"));
                break;
        }
    }

    public AnchorPane getScene() {
        return pane;
    }
}