package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.Task;
import com.cougarneticit.gims.model.common.Priority;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;


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
    @FXML JFXButton confirmButton;

    public RoomsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        confirmButton.setOnAction(e -> {

            Optional<Room> roomOptional = roomRepo.findById('A');
            Optional<Employee> employeeOptional = employeeRepo.findById(UUID.fromString("04d9a12e-e18c-4f9b-8269-48f829d8950d"));

            Room room = roomOptional.get();
            Employee employee = employeeOptional.get();
            Task task = new Task(UUID.randomUUID(), room, employee, Priority.HIGH, "12/12/2020 11:59 PM", "Description goes here");

            taskRepo.save(task);

        });
    }

    public AnchorPane getScene() {
        return pane;
    }
}