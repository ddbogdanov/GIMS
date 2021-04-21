package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Employee;
import com.cougarneticit.gims.model.Shift;
import com.cougarneticit.gims.model.Task;
import com.cougarneticit.gims.model.User;
import com.cougarneticit.gims.model.repos.EmployeeRepo;
import com.cougarneticit.gims.model.repos.ShiftRepo;
import com.cougarneticit.gims.model.repos.TaskRepo;
import com.cougarneticit.gims.model.repos.UserRepo;
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
import java.time.LocalDateTime;
import java.util.*;

@Component
@FxmlView("/EmployeeSceneController.fxml")
public class EmployeeSceneController extends GIMSController implements Initializable {

    //TODO: Move UserHelpLabel in line with the combobox.
    //TODO: Add help label to shifts form

    private Stage stage;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ShiftRepo shiftRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<User> userComboBox;
    @FXML private JFXComboBox<Employee> employeeComboBox;
    @FXML private JFXListView<Employee> employeeListView;
    @FXML private JFXListView<Shift> shiftListView;
    @FXML private JFXListView<Task> taskListView;
    @FXML private JFXDatePicker shiftStartDatePicker, shiftEndDatePicker;
    @FXML private JFXTimePicker shiftStartTimePicker, shiftEndTimePicker;
    @FXML private JFXTextField nameTextField, phoneTextField, emailTextField;

    @FXML private Label
            userHelpLabel, nameHelpLabel, phoneHelpLabel, emailHelpLabel,
            employeeFormLabel, shiftListLabel, shiftFormLabel, shiftFormHelpLabel,
            taskListLabel, activeTasksLabel, completedTasksLabel, emailLabel,
            phoneLabel, daysScheduledLabel, hoursScheduledLabel;

    @FXML private JFXButton
            viewEmployeeButton, editEmployeeButton, deleteEmployeeButton, employeeFormSubmitButton,  employeeFormCancelButton,
            shiftEditButton, shiftDeleteButton, shiftFormSubmitButton, shiftFormCancelButton;

    public EmployeeSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);

        populateUserComboBox();
        populateEmployeeComboBox();
        populateEmployeeListView();
        employeeListView.getSelectionModel().select(0);
        populateShiftListView(employeeListView.getSelectionModel().getSelectedItem().getEmployeeId(), employeeListView.getSelectionModel().getSelectedItem().getName());
        populateTaskListView(employeeListView.getSelectionModel().getSelectedItem().getEmployeeId(), employeeListView.getSelectionModel().getSelectedItem().getName());
        setInfoLabels(employeeListView.getSelectionModel().getSelectedItem());

        employeeFormSubmitButton.setOnAction(e -> {
            submitEmployee();
        });
        employeeFormCancelButton.setOnAction(e -> {
            resetEmployeeForm();
        });
        viewEmployeeButton.setOnAction(e -> {
            viewEmployee();
        });
        editEmployeeButton.setOnAction(e -> {
            editEmployee();
        });
        deleteEmployeeButton.setOnAction(e -> {
            deleteEmployee();
        });

        shiftFormSubmitButton.setOnAction(e -> {
            submitShift();
        });
        shiftFormCancelButton.setOnAction(e -> {
            resetShiftForm();
        });
        shiftEditButton.setOnAction(e -> {
            editShift();
        });
        shiftDeleteButton.setOnAction(e -> {
            deleteShift();
        });

        //Focus Listeners - Employee form
        employeeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                employeeListView.getSelectionModel().select(oldVal);
            }
            else {
                populateShiftListView(newVal.getEmployeeId(), newVal.getName());
                populateTaskListView(newVal.getEmployeeId(), newVal.getName());
                setInfoLabels(newVal);
            }
        });
        nameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> nameHelpLabel.setVisible(newVal));
        phoneTextField.focusedProperty().addListener((obs, oldVal, newVal) -> phoneHelpLabel.setVisible(newVal));
        emailTextField.focusedProperty().addListener((obs, oldVal, newVal) -> emailHelpLabel.setVisible(newVal));
        //Focus Listeners - Shifts form
        shiftStartDatePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                shiftStartDatePicker.getEditor().setText(shiftStartDatePicker.getConverter().toString(shiftStartDatePicker.getValue()));
            }
        });
        shiftStartTimePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                shiftStartTimePicker.getEditor().setText(shiftStartTimePicker.getConverter().toString(shiftStartTimePicker.getValue()));
            }
        });
        shiftEndDatePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                shiftEndDatePicker.getEditor().setText(shiftEndDatePicker.getConverter().toString(shiftEndDatePicker.getValue()));
            }
        });
        shiftEndTimePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                shiftEndTimePicker.getEditor().setText(shiftEndTimePicker.getConverter().toString(shiftEndTimePicker.getValue()));
            }
        });
    }

    //Button Actions - Employee form
    private void submitEmployee() {
        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();

        if(selectedUser.isEmployee()) {
            userHelpLabel.setTextFill(Color.web("#F73331"));
            userHelpLabel.setText("User already assigned an employee");
            userHelpLabel.setVisible(true);
            return;
        }

        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();

        if(validateEmployeeForm(name, phone, email)) {
            Employee emp = new Employee(UUID.randomUUID(), selectedUser, name, phone, email);
            employeeRepo.save(emp);

            populateEmployeeListView();
            resetEmployeeForm();
        }
    }
    private void viewEmployee() {
        resetEmployeeForm();
        userComboBox.setDisable(true);
        nameTextField.setDisable(true);
        phoneTextField.setDisable(true);
        emailTextField.setDisable(true);
        employeeFormSubmitButton.setDisable(true);

        employeeFormCancelButton.setText("Reset");
        employeeFormLabel.setText("View an Employee");

        populateEmployeeForm();
    }
    private void editEmployee() {
        resetEmployeeForm();
        try {
            employeeFormSubmitButton.setOnAction(e -> {
                submitEmployeeEdits();
            });

            populateEmployeeForm();
            employeeFormLabel.setText("Edit an Employee");
            employeeFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            //Revert to original event handler
            employeeFormSubmitButton.setOnAction(e -> {
                submitEmployee();
            });
            System.err.println("Nothing selected in employee list");
        }
    }
    private void submitEmployeeEdits() {
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();

        if(validateEmployeeForm(name, phone, email)) {
            Employee updatedEmployee = new Employee(
                    employeeListView.getSelectionModel().getSelectedItem().getEmployeeId(),
                    employeeListView.getSelectionModel().getSelectedItem().getUser(),
                    name, phone, email);
            employeeRepo.save(updatedEmployee);

            populateEmployeeListView();
            resetEmployeeForm();
        }
    }
    private void deleteEmployee() {
        //TODO add confirmation popup
        try {
            Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
            employeeRepo.deleteById(selectedEmployee.getEmployeeId());

            populateEmployeeListView();
            resetEmployeeForm();
        }
        catch(NullPointerException ex) {
            System.err.println("Nothing selected in employee list");
        }
    }
    private void resetEmployeeForm() {
        populateUserComboBox();
        userComboBox.getSelectionModel().clearSelection();
        userComboBox.setDisable(false);

        //Reset buttons
        employeeFormSubmitButton.setText("Add Employee");
        employeeFormSubmitButton.setDisable(false);
        employeeFormCancelButton.setText("Cancel");

        //Reset text fields
        nameTextField.clear();
        nameTextField.setDisable(false);
        phoneTextField.clear();
        phoneTextField.setDisable(false);
        emailTextField.clear();
        emailTextField.setDisable(false);

        //Reset labels
        employeeFormLabel.setText("Add an Employee");
        userHelpLabel.setTextFill(Color.web("#5BDDC7"));
        userHelpLabel.setText("User");
        userHelpLabel.setVisible(false);
        nameHelpLabel.setTextFill(Color.web("#5BDDC7"));
        nameHelpLabel.setText("First Last");
        nameHelpLabel.setVisible(false);
        phoneHelpLabel.setTextFill(Color.web("#5BDDC7"));
        phoneHelpLabel.setText("xxx-xxx-xxxx");
        phoneHelpLabel.setVisible(false);
        emailHelpLabel.setTextFill(Color.web("#5BDDC7"));
        emailHelpLabel.setText("email@domain.com");
        emailHelpLabel.setVisible(false);

        //Reset event handlers
        employeeFormSubmitButton.setOnAction(ee -> {
            submitEmployee();
        });
    }
    //Util Methods - Employee form
    private void populateEmployeeForm() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();

        for (User user : userComboBox.getItems()) {
            if (selectedEmployee.getUserId().equals(user.getUserId())) {
                userComboBox.getSelectionModel().select(user);
                userComboBox.setDisable(true);
                break;
            }
        }
        nameTextField.setText(selectedEmployee.getName());
        phoneTextField.setText(selectedEmployee.getPhone());
        emailTextField.setText(selectedEmployee.getEmail());
    }
    private void populateUserComboBox() {
        userComboBox.getItems().clear();
        ObservableList<User> userList = FXCollections.observableArrayList();

        if(userRepo.count() != 0) {
            userList.addAll(userRepo.findAll());
        }
        userComboBox.setItems(userList);
    }
    private void populateEmployeeListView() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();

        if(employeeRepo.count() != 0) {
            employeeList.addAll(employeeRepo.findAll());
        }
        employeeListView.setItems(employeeList.sorted());
    }
    private boolean validateEmployeeForm(String name, String phone, String email) {
        boolean isNameValid = validateName(name);
        boolean isPhoneValid = validatePhone(phone);
        boolean isEmailValid = validateEmail(email);

        if(!isNameValid) {
            nameHelpLabel.setTextFill(Color.web("#F73331"));
            nameHelpLabel.setText("Invalid format: First Last");
            nameHelpLabel.setVisible(true);
            return false;
        }
        else if(!isPhoneValid) {
            phoneHelpLabel.setTextFill(Color.web("#F73331"));
            phoneHelpLabel.setText("Invalid format: xxx-xxx-xxxx");
            phoneHelpLabel.setVisible(true);
            return false;
        }
        else if(!isEmailValid){
            emailHelpLabel.setTextFill(Color.web("#F73331"));
            emailHelpLabel.setText("Invalid format: email@domain.com");
            emailHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }
    }

    //Button Actions - Shift form
    private void submitShift() {
        if(validateShiftForm()) {
            LocalDateTime startDateTime = LocalDateTime.of(shiftStartDatePicker.getValue(), shiftStartTimePicker.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(shiftEndDatePicker.getValue(), shiftEndTimePicker.getValue());

            Shift shift = new Shift(
                    UUID.randomUUID(),
                    employeeComboBox.getValue(),
                    startDateTime,
                    endDateTime);
            shiftRepo.save(shift);

            populateShiftListView(employeeListView.getSelectionModel().getSelectedItem().getEmployeeId(), shiftListView.getSelectionModel().getSelectedItem().getEmployee().getName());
            resetShiftForm();
        }
    }
    private void resetShiftForm() {
        employeeComboBox.getSelectionModel().clearSelection();
        employeeComboBox.setDisable(false);

        shiftStartDatePicker.getEditor().clear();
        shiftStartDatePicker.setDisable(false);
        shiftStartTimePicker.getEditor().clear();
        shiftStartTimePicker.setDisable(false);
        shiftEndDatePicker.getEditor().clear();
        shiftEndDatePicker.setDisable(false);
        shiftEndTimePicker.getEditor().clear();
        shiftEndTimePicker.setDisable(false);

        shiftFormSubmitButton.setText("Schedule");

        shiftFormLabel.setText("Schedule an Employee");
        shiftFormHelpLabel.setVisible(false);

        shiftFormSubmitButton.setOnAction(e -> {
            submitShift();
        });
    }
    private void editShift() {
        resetShiftForm();

        try {
            shiftFormSubmitButton.setOnAction(e -> {
                submitShiftEdits();
            });

            populateShiftForm();
            shiftFormLabel.setText("Edit a Shift");
            shiftFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            shiftFormSubmitButton.setOnAction(e -> {
                submitShift();
            });
            System.err.println("Nothing selected in shift list");
        }
    }
    private void submitShiftEdits() {
        if(validateShiftForm()) {
            LocalDateTime startDateTime = LocalDateTime.of(shiftStartDatePicker.getValue(), shiftStartTimePicker.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(shiftEndDatePicker.getValue(), shiftEndTimePicker.getValue());

            Shift updatedShift = new Shift(
                    shiftListView.getSelectionModel().getSelectedItem().getShiftId(),
                    employeeComboBox.getValue(),
                    startDateTime,
                    endDateTime);
            shiftRepo.save(updatedShift);

            populateShiftListView(shiftListView.getSelectionModel().getSelectedItem().getEmployeeId(), shiftListView.getSelectionModel().getSelectedItem().getEmployee().getName());
            resetShiftForm();
        }
    }
    private void deleteShift() {
        try {
            Shift selectedShift = shiftListView.getSelectionModel().getSelectedItem();
            shiftRepo.deleteById(selectedShift.getShiftId());

            populateShiftListView(shiftListView.getSelectionModel().getSelectedItem().getEmployeeId(), shiftListView.getSelectionModel().getSelectedItem().getEmployee().getName());
            resetShiftForm();
        }
        catch(Exception ex) {
            System.err.println("Nothing selected in shift list");
        }
    }
    //Util Methods - Shift form
    private void populateShiftForm() {
        Shift selectedShift = shiftListView.getSelectionModel().getSelectedItem();

        for (Employee employee : employeeComboBox.getItems()) {
            if (selectedShift.getEmployeeId().equals(employee.getEmployeeId())) {
                employeeComboBox.getSelectionModel().select(employee);
                employeeComboBox.setDisable(true);
                break;
            }
        }

        LocalDateTime startDateTime = selectedShift.getStartDateTime();
        LocalDateTime endDateTime = selectedShift.getEndDateTime();

        shiftStartDatePicker.setValue(startDateTime.toLocalDate());
        shiftStartTimePicker.setValue(startDateTime.toLocalTime());
        shiftEndDatePicker.setValue(endDateTime.toLocalDate());
        shiftEndTimePicker.setValue(endDateTime.toLocalTime());
    }
    private void populateEmployeeComboBox() {
        employeeComboBox.getItems().clear();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        if(employeeRepo.count() != 0) {
            employeeList.addAll(employeeRepo.findAll());
        }
        employeeComboBox.getItems().addAll(employeeList);
    }
    private void populateShiftListView(UUID employeeId, String employeeName) {
        shiftListLabel.setText("Shift List - " + employeeName);
        ObservableList<Shift> shiftList = FXCollections.observableArrayList();

        if(shiftRepo.count() != 0) {
            shiftList.addAll(shiftRepo.findAllByEmployee_EmployeeId(employeeId));
        }
        shiftListView.setItems(shiftList.sorted());
    }
    private boolean validateShiftForm() {
        if(employeeComboBox.getSelectionModel().isEmpty()) {
            shiftFormHelpLabel.setVisible(true);
            //return false;
        }
        else if(shiftStartDatePicker.getValue() == null) {
            shiftFormHelpLabel.setVisible(true);
            return false;
        }
        else if(shiftStartTimePicker.getValue() == null) {
            shiftFormHelpLabel.setVisible(true);
            return false;
        }
        else if(shiftEndDatePicker.getValue() == null) {
            shiftFormHelpLabel.setVisible(true);
            return false;
        }
        else if(shiftEndTimePicker.getValue() == null) {
            shiftFormHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }
        return true;
    }

    //Util Methods - Extra
    private void populateTaskListView(UUID employeeId, String employeeName) {
        taskListLabel.setText("Task List - " + employeeName);
        ObservableList<Task> taskList = FXCollections.observableArrayList();

        if(taskRepo.count() != 0) {
            taskList.addAll(taskRepo.findAllByEmployee_EmployeeId(employeeId));
        }
        taskListView.setItems(taskList.sorted());
    }
    private void setInfoLabels(Employee employee) {
        //TODO: count tasks, days and hours scheduled
        emailLabel.setText(employee.getEmail());
        phoneLabel.setText(employee.getPhone());
    }

    public AnchorPane getScene() { return pane; }

}