package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Room;
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
    @FXML private JFXListView<Room> roomListView;
    @FXML private JFXToggleButton editToggleButton;
    @FXML private JFXTextField roomIdTextField, roomNameTextField, taskNameTextField;
    @FXML private JFXTextArea taskDescriptionTextArea;
    @FXML private Label roomFormLabel, roomNameLabel, activeTasksLabel, statusLabel, roomIdHelpLabel, roomNameHelpLabel, taskNameHelpLabel, taskDescriptionHelpLabel;
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
                    roomFormSubmitButton.setOnAction(ee -> {
                        submitRoom();
                    });
                });

                populateRoomForm();
                roomFormLabel.setText("Edit a Room");
                roomFormSubmitButton.setText("Submit");
            }
            catch(NullPointerException ex) {
                roomFormSubmitButton.setOnAction(ee -> {
                    submitRoom();
                });
                System.err.println("Nothing selected");
                //TODO
            }
        }
        else {
            resetRoomForm();
        }
    }
    private void submitRoomEdits() {
        Room updatedRoom = new Room(
                roomIdTextField.getText().charAt(0),
                roomNameTextField.getText(),
                RoomStatus.VACANT); //TODO: Accept RoomStatus in Room Form
        roomRepo.save(updatedRoom);

        populateRoomListView();
        resetRoomForm();
    }
    private void submitRoom() {
        if(!roomRepo.existsById(roomIdTextField.getText().charAt(0))) {
            Room room = new Room(roomIdTextField.getText().charAt(0), roomNameTextField.getText(), RoomStatus.VACANT);
            roomRepo.save(room);

            populateRoomListView();
            resetRoomForm();
        }
        else {
            //TODO
            System.err.println("already exists");
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
            //TODO
            System.err.println("nothing selected in list");
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
        roomIdTextField.setDisable(true);
        roomNameTextField.setText(selectedRoom.getRoomName());
    }
    private void resetRoomForm() {
        editToggleButton.selectedProperty().setValue(false);
        roomFormSubmitButton.setText("Add Room");

        roomFormLabel.setText("Add a Room");

        roomIdTextField.clear();
        roomIdTextField.setDisable(false);
        roomNameTextField.clear();

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