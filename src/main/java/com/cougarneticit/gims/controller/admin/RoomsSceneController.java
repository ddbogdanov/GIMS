package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.*;
import com.cougarneticit.gims.model.repos.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


@Component
@FxmlView("/RoomsSceneController.fxml")
public class RoomsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    TaskRepo taskRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    RoomRateRepo roomRateRepo;
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    PriorityRepo priorityRepo;
    @Autowired
    RoomStatusRepo roomStatusRepo;
    @Autowired
    RoomReportRepo roomReportRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<Priority> priorityComboBox;
    @FXML private JFXComboBox<RoomStatus> roomStatusComboBox;
    @FXML private JFXComboBox<String> roomRateComboBox;
    @FXML private JFXComboBox<Room> roomComboBox;
    @FXML private JFXComboBox<Employee> employeeComboBox;
    @FXML private JFXDatePicker dueDatePicker;
    @FXML private JFXListView<Room> roomListView;
    @FXML private JFXListView<Task> taskListView;
    @FXML private JFXToggleButton editToggleButton;
    @FXML private JFXTextField roomIdTextField, roomNameTextField, taskNameTextField;
    @FXML private JFXTextArea taskDescriptionTextArea;

    @FXML private Label
            roomFormLabel, roomNameLabel, roomStatusHelpLabel, activeTasksLabel,
            statusLabel, roomIdHelpLabel, roomNameHelpLabel, roomHelpLabel, taskFormLabel,
            employeeHelpLabel, priorityHelpLabel, taskNameHelpLabel, taskDescriptionHelpLabel;

    @FXML private JFXButton
            roomFormSubmitButton, roomFormCancelButton, roomFormDeleteButton, refreshTaskFormButton,
            taskEditButton, taskViewButton, taskDeleteButton, taskFormSubmitButton, taskFormCancelButton, roomReportButton, deleteRateButton;

    public RoomsSceneController(FxWeaver fxWeaver) {
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
        }
        catch(NullPointerException ex) {
            System.err.println("No rooms");
        }
        //Initialize ComboBoxes
        priorityComboBox.getItems().addAll(priorityRepo.findAll());
        roomStatusComboBox.getItems().addAll(roomStatusRepo.findAll());
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
        populateRoomRateComboBox();

        //Room Form Actions
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
        roomReportButton.setOnAction(e -> {
            generateRoomReport();
        });
        deleteRateButton.setOnAction(e -> {
            deleteRate();
        });

        //Task Form Actions
        refreshTaskFormButton.setOnAction(e -> {
            refreshTaskForm();
        });
        taskFormSubmitButton.setOnAction(e -> {
            submitTask();
        });
        taskFormCancelButton.setOnAction(e -> {
            resetTaskForm();
        });
        taskEditButton.setOnAction(e -> {
            editTask();
        });
        taskViewButton.setOnAction(e -> {
            viewTask();
        });
        taskDeleteButton.setOnAction(e -> {
            deleteTask();
        });

        //Focus Listeners - Room form
        roomIdTextField.focusedProperty().addListener((obs, oldVal, newVal) -> roomIdHelpLabel.setVisible(newVal));
        roomNameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> roomNameHelpLabel.setVisible(newVal));
        taskNameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> taskNameHelpLabel.setVisible(newVal));
        taskDescriptionTextArea.focusedProperty().addListener((obs, oldVal, newVal) -> taskDescriptionHelpLabel.setVisible(newVal));
        roomListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                roomListView.getSelectionModel().select(oldVal);
            }
            else {
                setInfoLabels();
                populateTaskListView(newVal.getRoomId());
            }
            if(editToggleButton.isSelected()) {
                populateRoomForm();
            }
        });
        roomIdTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.length() > 1) {
                roomIdTextField.setText(oldVal);
            }
        });
        //Focus Listeners - Task form
        taskNameTextField.focusedProperty().addListener((obs, oldVal, newVal) -> taskNameHelpLabel.setVisible(newVal));
        taskDescriptionTextArea.focusedProperty().addListener((obs, oldVal, newVal) -> taskDescriptionHelpLabel.setVisible(newVal));
        dueDatePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                dueDatePicker.getEditor().setText(dueDatePicker.getConverter().toString(dueDatePicker.getValue()));
            }
        });
    }

    //Button Actions - Room form
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
            RoomRate rate = getOrCreateRate();
            Room updatedRoom = new Room(
                    roomIdTextField.getText().charAt(0),
                    roomNameTextField.getText(),
                    roomStatusComboBox.getValue(),
                    rate); //TODO assign rate
            roomRepo.save(updatedRoom);

            populateRoomListView();
            roomListView.getSelectionModel().select(updatedRoom);
            roomListView.getFocusModel().focus(roomListView.getItems().indexOf(updatedRoom));
            roomListView.scrollTo(updatedRoom);
            setInfoLabels();
            resetRoomForm();
        }
    }
    private void submitRoom() {
        if(validateRoomForm()) {
            if (!roomRepo.existsById(roomIdTextField.getText().charAt(0))) {
                RoomRate rate = getOrCreateRate();

                Room room = new Room(roomIdTextField.getText().charAt(0), roomNameTextField.getText(), roomStatusComboBox.getValue(), rate); //TODO assign rate
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
    private void resetRoomForm() {
        roomStatusComboBox.getSelectionModel().clearSelection();
        roomRateComboBox.getSelectionModel().clearSelection();
        roomRateComboBox.setValue(null);

        editToggleButton.selectedProperty().setValue(false);
        roomFormSubmitButton.setText("Add Room");

        roomIdTextField.clear();
        roomIdTextField.setDisable(false);
        roomNameTextField.clear();

        roomFormLabel.setText("Add a Room");
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
    private void generateRoomReport() {
        final Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        final Font subHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        final Font bodyFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();
        LocalDate date = LocalDate.now();

        try {
            Document doc = new Document();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("RoomReport" + "_Suite_" + selectedRoom.getRoomId() + "_" + date.toString());

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showSaveDialog(stage);
            PdfWriter.getInstance(doc, new FileOutputStream(file));

            doc.open();

            //Create a title table for PDF
            Paragraph title = new Paragraph();
            title.add(new Paragraph("Suite: " + selectedRoom.getRoomId() + " Room Report - Generated on: " + date.toString(), headingFont));
            title.setAlignment(Element.ALIGN_LEFT);
            Image gimsLogo = Image.getInstance("src/main/resources/static/img/Logo1png.png");
            gimsLogo.scaleToFit(50, 50);
            gimsLogo.setAlignment(Element.ALIGN_RIGHT);
            PdfPCell leftTitleCell = new PdfPCell();
            PdfPCell rightTitleCell = new PdfPCell();
            rightTitleCell.setBorder(Rectangle.NO_BORDER);
            leftTitleCell.setBorder(Rectangle.NO_BORDER);
            PdfPTable titleTable = new PdfPTable(2);
            titleTable.setWidthPercentage(100f);
            leftTitleCell.addElement(title);
            rightTitleCell.addElement(gimsLogo);
            titleTable.addCell(leftTitleCell);
            titleTable.addCell(rightTitleCell);

            //Create a task count table for PDF
            int activeTasks = taskRepo.countAllByRoom_RoomIdAndCompleted(selectedRoom.getRoomId(), false);
            int completedTasks = taskRepo.countAllByRoom_RoomIdAndCompleted(selectedRoom.getRoomId(), true);
            PdfPTable taskCountTable = new PdfPTable(2);
            taskCountTable.setWidthPercentage(100f);
            taskCountTable.setSpacingBefore(30f);
            taskCountTable.setSpacingAfter(30f);
            taskCountTable.addCell(new PdfPCell(new Paragraph("Active Tasks", subHeadingFont)));
            taskCountTable.addCell(new PdfPCell(new Paragraph("Completed Tasks", subHeadingFont)));
            taskCountTable.addCell(new PdfPCell(Phrase.getInstance(String.valueOf(activeTasks))));
            taskCountTable.addCell(new PdfPCell(Phrase.getInstance(String.valueOf(completedTasks))));

            //TODO: Add more stuff

            doc.addTitle("Suite: " + selectedRoom.getRoomId() + " Room Report - Generated:  " + date.toString());
            doc.addCreationDate();
            doc.add(titleTable);
            doc.add(taskCountTable);

            roomReportRepo.save(new RoomReport(UUID.randomUUID(), selectedRoom, date));

            doc.close();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.err.println("FileChooser closed without selecting path, or File Not Found");
        }
    }
    private void deleteRate() {
        if(!roomRateComboBox.getValue().isBlank()) {
            roomRateRepo.deleteByRate(new BigDecimal(roomRateComboBox.getValue()));
            populateRoomRateComboBox();
            populateRoomListView();
        }
    }

    //Util Methods - Room form
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

        for (RoomStatus status : roomStatusComboBox.getItems()) {
            if (selectedRoom.getRoomStatusId() == status.getRoomStatusId()) {
                roomStatusComboBox.getSelectionModel().select(status);
                break;
            }
        }
        roomRateComboBox.setValue(selectedRoom.getRoomRate().toString());
    }
    private void populateRoomRateComboBox() {
        roomRateComboBox.getItems().clear();
        ObservableList<String> roomRateStringList = FXCollections.observableArrayList();
        if(roomRateRepo.count() != 0) {
            List<RoomRate> roomRates = roomRateRepo.findAll();
            for(RoomRate rr : roomRates) {
                roomRateStringList.add(rr.getRoomStringRate());
            }
        }
        roomRateComboBox.setItems(roomRateStringList);
    }
    private RoomRate getOrCreateRate() {
        RoomRate rate;
        if(roomRateRepo.existsByRate(new BigDecimal(roomRateComboBox.getValue()))) {
            rate = roomRateRepo.findByRate(new BigDecimal(roomRateComboBox.getValue()));
        }
        else {
            rate = new RoomRate(new BigDecimal(roomRateComboBox.getValue()));
            roomRateRepo.save(rate);
            populateRoomRateComboBox();
        }
        return rate;
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

    //Button Actions - Task form
    private void submitTask() {
        if (validateTaskForm()) {
            Task task = new Task(
                    UUID.randomUUID(),
                    roomComboBox.getValue(),
                    employeeComboBox.getValue(),
                    taskNameTextField.getText(),
                    priorityComboBox.getValue(),
                    dueDatePicker.getValue().toString(),
                    taskDescriptionTextArea.getText());
            taskRepo.save(task);

            populateTaskListView(roomComboBox.getValue().getRoomId());
            setInfoLabels();
            resetTaskForm();
        }
    }
    private void resetTaskForm() {
        roomComboBox.setDisable(false);
        roomComboBox.getSelectionModel().clearSelection();
        employeeComboBox.setDisable(false);
        employeeComboBox.getSelectionModel().clearSelection();
        priorityComboBox.setDisable(false);
        priorityComboBox.getSelectionModel().clearSelection();

        dueDatePicker.setDisable(false);
        dueDatePicker.getEditor().clear();

        taskFormSubmitButton.setDisable(false);
        taskFormSubmitButton.setText("Add Task");
        taskFormCancelButton.setDisable(false);
        taskFormCancelButton.setText("Cancel");

        taskNameTextField.setDisable(false);
        taskNameTextField.clear();
        taskDescriptionTextArea.setDisable(false);
        taskDescriptionTextArea.clear();

        taskFormLabel.setText("Add a Task");
        roomHelpLabel.setText("Room");
        roomHelpLabel.setTextFill(Color.web("#5BDDC7"));
        roomHelpLabel.setVisible(false);
        employeeHelpLabel.setText("Employee");
        employeeHelpLabel.setTextFill(Color.web("#5BDDC7"));
        employeeHelpLabel.setVisible(false);
        taskNameHelpLabel.setText("48 Character Max");
        taskNameHelpLabel.setTextFill(Color.web("#5BDDC7"));
        taskNameHelpLabel.setVisible(false);
        priorityHelpLabel.setText("Priority");
        priorityHelpLabel.setTextFill(Color.web("#5BDDC7"));
        priorityHelpLabel.setVisible(false);
        taskDescriptionHelpLabel.setText("256 Character Max");
        taskDescriptionHelpLabel.setTextFill(Color.web("#5BDDC7"));
        taskDescriptionHelpLabel.setVisible(false);

        taskFormSubmitButton.setOnAction(e -> {
            submitTask();
        });
    }
    private void editTask() {
        try {
            taskFormSubmitButton.setOnAction(e -> {
                submitTaskEdits();
            });

            populateTaskForm();
            roomComboBox.setDisable(true);
            taskFormLabel.setText("Edit a Task");
            taskFormSubmitButton.setText("Submit");
        }
        catch(NullPointerException ex) {
            taskFormSubmitButton.setOnAction(e -> {
                submitTask();
            });
            System.err.println("Nothing selected in task list");
        }
    }
    private void submitTaskEdits() {
        if(validateTaskForm()) {
            Task updatedTask = new Task(
                    taskListView.getSelectionModel().getSelectedItem().getTaskId(),
                    roomComboBox.getValue(),
                    employeeComboBox.getValue(),
                    taskNameTextField.getText(),
                    priorityComboBox.getValue(),
                    dueDatePicker.getValue().toString(),
                    taskDescriptionTextArea.getText());
            taskRepo.save(updatedTask);

            populateTaskListView(roomListView.getSelectionModel().getSelectedItem().getRoomId());
            resetTaskForm();
            refreshTaskForm();
        }
    }
    private void viewTask() {
        resetTaskForm();
        taskFormLabel.setText("View a Task");
        roomComboBox.setDisable(true);
        employeeComboBox.setDisable(true);
        taskNameTextField.setDisable(true);
        priorityComboBox.setDisable(true);
        dueDatePicker.setDisable(true);
        taskDescriptionTextArea.setDisable(true);
        taskFormSubmitButton.setDisable(true);
        taskFormCancelButton.setText("Reset");

        populateTaskForm();
    }
    private void deleteTask() {
        try {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            taskRepo.deleteById(selectedTask.getTaskId());


            populateTaskListView(roomListView.getSelectionModel().getSelectedItem().getRoomId());
            resetTaskForm();
        }
        catch(Exception ex) {
            System.err.println("Nothing selected in list");
        }
    }
    private void refreshTaskForm() {
        roomComboBox.getItems().clear();
        employeeComboBox.getItems().clear();
        dueDatePicker.getEditor().clear();
        roomComboBox.getItems().addAll(roomRepo.findAll());
        employeeComboBox.getItems().addAll(employeeRepo.findAll());
    }
    //Util Methods - Task form
    private boolean validateTaskForm() {
        if(roomComboBox.getSelectionModel().isEmpty()) {
            roomHelpLabel.setText("Select a room");
            roomHelpLabel.setTextFill(Color.web("#F73331"));
            roomHelpLabel.setVisible(true);
            return false;
        }
        else if(employeeComboBox.getSelectionModel().isEmpty()) {
            employeeHelpLabel.setText("Select an employee");
            employeeHelpLabel.setTextFill(Color.web("#F73331"));
            employeeHelpLabel.setVisible(true);
            return false;
        }
        else if(taskNameTextField.getText().isBlank()) {
            taskNameHelpLabel.setText("Task name cannot be blank");
            taskNameHelpLabel.setTextFill(Color.web("#F73331"));
            taskNameHelpLabel.setVisible(true);
            return false;
        }
        else if(priorityComboBox.getSelectionModel().isEmpty()) {
            priorityHelpLabel.setText("Select a priority");
            priorityHelpLabel.setTextFill(Color.web("#F73331"));
            priorityHelpLabel.setVisible(true);
            return false;
        }
        else if(taskDescriptionTextArea.getText().isBlank()) {
            taskDescriptionHelpLabel.setText("Description cannot be blank");
            taskDescriptionHelpLabel.setTextFill(Color.web("#F73331"));
            taskDescriptionHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }
    }
    private void populateTaskListView(char roomId) {
        ObservableList<Task> taskList = FXCollections.observableArrayList();

        if(taskRepo.count() != 0){
            taskList.addAll(taskRepo.findAllByRoom_RoomId(roomId));
        }
        taskListView.setItems(taskList.sorted());
    }
    private void populateTaskForm() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();

        populateTaskFormRoomComboBox(selectedTask);
        populateTaskFormEmployeeComboBox(selectedTask);
        taskNameTextField.setText(selectedTask.getName());

        for (Priority priority : priorityComboBox.getItems()) {
            if (selectedTask.getPriorityId() == priority.getPriorityId()) {
                priorityComboBox.getSelectionModel().select(priority);
                break;
            }
        }

        dueDatePicker.setValue(LocalDate.parse(selectedTask.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        taskDescriptionTextArea.setText(selectedTask.getDescription());
    }
    private void populateTaskFormRoomComboBox(Task selectedTask) {
        //Loop through rooms in the roomComboBox
        for(Room room : roomComboBox.getItems()) {
            //Fetch list of tasks for each room in the roomComboBox
            Set<Task> taskIds = room.getTasks();
            //Loop through each task from above list
            for(Task task : taskIds) {
                //If selected task ID in taskListView equals task a ID from the list of tasks - select it
                if(selectedTask.getTaskId().equals(task.getTaskId())) {
                    roomComboBox.getSelectionModel().select(room);
                    return;
                }
            }
        }
    }
    private void populateTaskFormEmployeeComboBox(Task selectedTask) {
        for(Employee employee : employeeComboBox.getItems()) {
            Set<Task> taskIds = employee.getTasks();
            for(Task task : taskIds) {
                if(selectedTask.getTaskId().equals(task.getTaskId())) {
                    employeeComboBox.getSelectionModel().select(employee);
                    return;
                }
            }
        }
    }
    private void setInfoLabels() {
        Room selectedRoom = roomListView.getSelectionModel().getSelectedItem();

        roomNameLabel.setText(selectedRoom.getRoomId() + ": " + selectedRoom.getRoomName());
        activeTasksLabel.setText(String.valueOf(taskRepo.countAllByRoom_RoomId(selectedRoom.getRoomId())));
        statusLabel.setText(String.valueOf(selectedRoom.getRoomStatus()));
        switch(selectedRoom.getRoomStatus().getRoomStatus()) {
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

    public AnchorPane getScene() {
        return pane;
    }
}