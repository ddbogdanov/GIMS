package com.cougarneticit.gims.controller.nonadmin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Clocking;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Shift;
import com.cougarneticit.gims.model.repos.ClockingRepo;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.ShiftRepo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@FxmlView("/EmpShiftsSceneController.fxml")
public class EmpShiftsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    ShiftRepo shiftRepo;
    @Autowired
    ClockingRepo clockingRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Shift> shiftListView;

    @FXML private Label
            shiftStartDateLabel, shiftStartTimeLabel, shiftEndDateLabel, shiftEndTimeLabel,
            clockedInLabel, clockedOutLabel;

    @FXML private JFXButton clockInButton, clockOutButton;

    public EmpShiftsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        try {
            populateShiftListView();
            shiftListView.getSelectionModel().select(0);
            populateInfoLabels(shiftListView.getSelectionModel().getSelectedItem());
            alterButtonDisableProperty(shiftListView.getSelectionModel().getSelectedItem());
        }
        catch(NullPointerException ex) {
            System.err.println("No shifts available");
        }

        clockInButton.setOnAction(e -> {
            clockIn();
        });
        clockOutButton.setOnAction(e -> {
            clockOut();
        });

        shiftListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                populateInfoLabels(newVal);
                alterButtonDisableProperty(newVal);
            }
        });
    }

    private void clockIn() {
        Shift selectedShift = shiftListView.getSelectionModel().getSelectedItem();
        Clocking selectedClocking = selectedShift.getClocking();

        int oldIndex = shiftListView.getSelectionModel().getSelectedIndex();

        selectedClocking.clockIn(LocalDateTime.now());
        clockingRepo.save(selectedClocking);

        populateShiftListView();
        shiftListView.getSelectionModel().select(oldIndex);
        populateInfoLabels(selectedShift);
    }
    private void clockOut() {
        Shift selectedShift = shiftListView.getSelectionModel().getSelectedItem();
        Clocking selectedClocking = selectedShift.getClocking();

        int oldIndex = shiftListView.getSelectionModel().getSelectedIndex();

        selectedClocking.clockOut(LocalDateTime.now());
        clockingRepo.save(selectedClocking);

        populateShiftListView();
        shiftListView.getSelectionModel().select(oldIndex);
        populateInfoLabels(selectedShift);
    }
    private void alterButtonDisableProperty(Shift newVal) {
        if(newVal.isClockedOut() && newVal.isClockedOut()) {
            clockInButton.setDisable(true);
            clockOutButton.setDisable(true);
        }
        else if(newVal.isClockedIn()) {
            clockInButton.setDisable(true);
            clockOutButton.setDisable(false);
        }
        else {
            clockInButton.setDisable(false);
            clockOutButton.setDisable(true);
        }
    }
    private void populateInfoLabels(Shift newVal) {
        LocalDate startDate = newVal.getStartDateTime().toLocalDate();
        LocalTime startTime = newVal.getStartDateTime().toLocalTime();
        LocalDate endDate = newVal.getEndDateTime().toLocalDate();
        LocalTime endTime = newVal.getEndDateTime().toLocalTime();

        String startDateString = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));
        String startTimeString = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));
        String endDateString = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));
        String endTimeString = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));

        shiftStartDateLabel.setText(startDateString);
        shiftStartTimeLabel.setText(startTimeString);
        shiftEndDateLabel.setText(endDateString);
        shiftEndTimeLabel.setText(endTimeString);

        clockedInLabel.setText(newVal.isClockedIn() ? "Yes" : "No");
        clockedOutLabel.setText(newVal.isClockedOut() ? "Yes" : "No");
    }
    private void populateShiftListView() {
        ObservableList<Shift> shiftList = FXCollections.observableArrayList();
        Employee activeEmployee = employeeRepo.findByUser_UserId(getActiveUser().getUserId());

        if(shiftRepo.count() != 0) {
            shiftList.addAll(shiftRepo.findAllByEmployee_EmployeeId(activeEmployee.getEmployeeId()));
        }
        shiftListView.setItems(shiftList.sorted());
    }

    public AnchorPane getScene() {
        return pane;
    }
}
