package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Customer;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.common.RoomStatus;
import com.cougarneticit.gims.model.repos.CustomerRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.UserRepo;
import com.cougarneticit.gims.validators.FieldValidator;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Component
@FxmlView("/CustomersSceneController.fxml")
public class CustomersSceneController extends GIMSController implements Initializable {

    private Stage stage;


    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private RoomRepo roomRepo;


    @FXML private AnchorPane pane;
    @FXML JFXButton confirmCustomer, cancelCustomerBooking, viewCustomerButton, editCustomerButton, deleteCustomerButton;
    @FXML private JFXListView<Customer> customerListView;
    @FXML private JFXComboBox<Room> customerRoomComboBox;
    @FXML private JFXTextField customerName, customerPhone, customerEmail;
    @FXML private JFXTextArea extraInfo;
    @FXML private JFXDatePicker startDate, endDate;
    @FXML private Label customerNameHelpLabel, customerPhoneHelpLabel, customerEmailHelpLabel, customerListViewLabel1, customerListViewLabel2, customerOperationLabel;

    public CustomersSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateRoomComboBox();
        populateCustomerList();

        confirmCustomer.setOnAction(e -> {
            addCustomer();
        });

        viewCustomerButton.setOnAction(e -> {
            viewCustomer();
        });

        editCustomerButton.setOnAction(e -> {

            editCustomer();
        });

        deleteCustomerButton.setOnAction(e -> {
            deleteCustomer();
        });

        cancelCustomerBooking.setOnAction(e -> {
            resetCustomerForm();
        });
    }

    private void addCustomer() {
        Room selectedRoom = customerRoomComboBox.getSelectionModel().getSelectedItem();
        String name = customerName.getText();
        String phone = customerPhone.getText();
        String email = customerEmail.getText();
        String extraInformation = extraInfo.getText();
        Date start = Date.from(startDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
        Date end = Date.from(endDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));

        boolean isNameValid = FieldValidator.validateName(name);
        boolean isPhoneValid = FieldValidator.validatePhone(phone);
        boolean isEmailValid = FieldValidator.validateEmail(email);

        if (isNameValid && isPhoneValid && isEmailValid) {
            Customer customer = null;
            if(confirmCustomer.getText().equals("Save Changes")) {
                customer = customerListView.getSelectionModel().getSelectedItem();
                customer.setCustomerEmail(email);
                customer.setCustomerName(name);
                customer.setCustomerPhone(phone);
                customer.setExtraInfo(extraInformation);
                customer.setStartDate(start);
                customer.setEndDate(end);
                if(Objects.nonNull(customer.getRoomId())) {
                    Room room = roomRepo.findById(customer.getRoomId()).orElse(null);
                    if(Objects.nonNull(room)) {
                        room.setCustomer(null);
                        room.setStatus(RoomStatus.VACANT);
                    }
                }
            } else {
                customer = new Customer(UUID.randomUUID(), name, phone, email, extraInformation, start, end);
            }
            customer.setRoom(selectedRoom.getRoomId());
            selectedRoom.setCustomer(customerRepo.save(customer));
            selectedRoom.setStatus(RoomStatus.OCCUPIED);
            roomRepo.save(selectedRoom);
            populateCustomerList();
            populateRoomComboBox();
            resetCustomerForm();
        }
        else {
            if(!isNameValid) {
                customerNameHelpLabel.setTextFill(Color.web("#F73331"));
                customerNameHelpLabel.setText("Invalid format: First Last");
                customerNameHelpLabel.setVisible(true);
            } else {
                customerNameHelpLabel.setVisible(false);
            }
            if(!isPhoneValid) {
                customerPhoneHelpLabel.setTextFill(Color.web("#F73331"));
                customerPhoneHelpLabel.setText("Invalid format: xxx-xxx-xxxx");
                customerPhoneHelpLabel.setVisible(true);
            } else {
                customerPhoneHelpLabel.setVisible(false);
            }
            if(!isEmailValid) {
                customerEmailHelpLabel.setTextFill(Color.web("#F73331"));
                customerEmailHelpLabel.setText("Invalid format: email@domain.com");
                customerEmailHelpLabel.setVisible(true);
            } else {
                customerPhoneHelpLabel.setVisible(false);
            }
        }
    }

    private void viewCustomer() {
        customerOperationLabel.setText("Viewing Customer");
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        for (Room room : customerRoomComboBox.getItems()) {
            if (room.getRoomId() == selectedCustomer.getRoomId()) {
                customerRoomComboBox.getSelectionModel().select(room);
                break;
            }
        }
        customerRoomComboBox.setEditable(false);
        customerName.setText(selectedCustomer.getCustomerName());
        customerName.setEditable(false);
        customerPhone.setText(selectedCustomer.getCustomerPhone());
        customerPhone.setEditable(false);
        customerEmail.setText(selectedCustomer.getCustomerEmail());
        customerEmail.setEditable(false);
        extraInfo.setText(selectedCustomer.getExtraInfo());
        extraInfo.setEditable(false);
        startDate.setValue(selectedCustomer.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startDate.setDisable(false);
        endDate.setValue(selectedCustomer.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        endDate.setDisable(false);
        confirmCustomer.setVisible(false);
    }

    private void editCustomer() {
        resetCustomerForm();
        customerOperationLabel.setText("Edit Customer");
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        for (Room room : customerRoomComboBox.getItems()) {
            if (room.getRoomId() == selectedCustomer.getRoomId()) {
                customerRoomComboBox.getSelectionModel().select(room);
                break;
            }
        }
        customerName.setText(selectedCustomer.getCustomerName());
        customerPhone.setText(selectedCustomer.getCustomerPhone());
        customerEmail.setText(selectedCustomer.getCustomerEmail());
        extraInfo.setText(selectedCustomer.getExtraInfo());
        startDate.setValue(selectedCustomer.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        endDate.setValue(selectedCustomer.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        confirmCustomer.setVisible(true);
        confirmCustomer.setText("Save Changes");
    }

    private void deleteCustomer() {
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
        if(Objects.nonNull(selectedCustomer)) {
            Room room = roomRepo.findById(selectedCustomer.getRoomId()).orElse(null);
            if(Objects.nonNull(room)) {
                room.setCustomer(null);
                room.setStatus(RoomStatus.VACANT);
                roomRepo.save(room);
            }
            customerRepo.delete(selectedCustomer);
        }
        resetCustomerForm();
        populateCustomerList();
    }

    private void populateRoomComboBox() {
        customerRoomComboBox.getItems().clear();
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        if(roomRepo.count() != 0) {
            rooms.addAll(roomRepo.findAll());
        }
        customerRoomComboBox.getItems().addAll(rooms);
    }

    private void populateCustomerList() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        if(customerRepo.count() != 0) {
            customers.addAll(customerRepo.findAll());
            customerListViewLabel1.setText("");
            customerListViewLabel2.setText("");
            customerListViewLabel1.setDisable(true);
            customerListViewLabel2.setDisable(true);
        } else {
            customerListViewLabel1.setDisable(false);
            customerListViewLabel2.setDisable(false);
        }
        customerListView.setItems(customers.sorted());
    }

    private void resetCustomerForm() {
        customerOperationLabel.setText("Add Customer");
        customerRoomComboBox.getSelectionModel().clearSelection();
        customerRoomComboBox.setDisable(false);

        //Reset buttons
        confirmCustomer.setText("Add Employee");
        confirmCustomer.setDisable(false);
        cancelCustomerBooking.setText("Cancel");
        //Reset text fields
        customerName.clear();
        customerName.setDisable(false);
        customerName.setEditable(true);
        customerPhone.clear();
        customerPhone.setDisable(false);
        customerPhone.setEditable(true);
        customerEmail.clear();
        customerEmail.setDisable(false);
        customerEmail.setEditable(true);
        extraInfo.clear();
        extraInfo.setDisable(false);
        extraInfo.setEditable(true);
        startDate.setValue(LocalDate.now());
        startDate.setDisable(false);
        endDate.setValue(LocalDate.now());
        endDate.setDisable(false);
        //Reset labels
        customerNameHelpLabel.setTextFill(Color.web("#5BDDC7"));
        customerNameHelpLabel.setText("First Last");
        customerNameHelpLabel.setVisible(false);
        customerPhoneHelpLabel.setTextFill(Color.web("#5BDDC7"));
        customerPhoneHelpLabel.setText("xxx-xxx-xxxx");
        customerPhoneHelpLabel.setVisible(false);
        customerEmailHelpLabel.setTextFill(Color.web("#5BDDC7"));
        customerEmailHelpLabel.setText("email@domain.com");
        customerEmailHelpLabel.setVisible(false);

    }

    public AnchorPane getScene() {
        return pane;
    }
}
