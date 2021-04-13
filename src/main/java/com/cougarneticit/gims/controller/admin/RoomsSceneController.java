package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
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
    @FXML private JFXTextField roomIdTextField, roomNameTextField;
    @FXML private JFXButton roomFormConfirmButton, roomFormCancelButton;

    public RoomsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        populateRoomListView();

        roomListView.getSelectionModel().selectedItemProperty().addListener((observableValue, room, t1) -> {
            if(editToggleButton.isSelected()) {
                populateRoomForm();
            }
        });

        editToggleButton.setOnAction(e -> {
            if(editToggleButton.isSelected()) {
                populateRoomForm();
            }
            else {
                resetRoomForm();
            }
        });
    }

    public void populateRoomListView() {
        ObservableList<Room> roomList = FXCollections.observableArrayList();

        if(roomRepo.count() != 0) {
            roomList.addAll(roomRepo.findAll());
        }
        roomListView.setItems(roomList.sorted());
    }
    public void populateRoomForm() {
        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();

        roomIdTextField.setText(String.valueOf(selectedRoom.getRoomId()));
        roomNameTextField.setText(selectedRoom.getRoomName());
    }
    public void resetRoomForm() {
        roomIdTextField.clear();
        roomNameTextField.clear();
    }

    public AnchorPane getScene() {
        return pane;
    }
}