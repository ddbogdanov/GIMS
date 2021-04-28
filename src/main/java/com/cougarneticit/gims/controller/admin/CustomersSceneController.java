package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Customer;
import com.cougarneticit.gims.model.Event;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.common.RoomStatus;
import com.cougarneticit.gims.model.repos.CustomerRepo;
import com.cougarneticit.gims.model.repos.EventRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
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
import java.time.ZoneOffset;
import java.util.*;

@Component
@FxmlView("/CustomersSceneController.fxml")
public class CustomersSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired private CustomerRepo customerRepo;
    @Autowired private RoomRepo roomRepo;
    @Autowired private EventRepo eventRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXButton confirmCustomer, cancelCustomerBooking, viewCustomerButton, editCustomerButton, deleteCustomerButton, customerRefresh, eventReg, cancelEventReg;
    @FXML private JFXListView<Customer> customerListView;
    @FXML private JFXComboBox<Room> customerRoomComboBox;
    @FXML private JFXComboBox<Event> eventRegComboBox;
    @FXML private JFXTextField customerName, customerPhone, customerEmail, customerState, customerCountry;
    @FXML private JFXTextArea extraInfo;
    @FXML private JFXDatePicker startDate, endDate;
    @FXML private Label customerNameHelpLabel, customerPhoneHelpLabel, customerEmailHelpLabel, customerStateHelpLabel, customerCountryHelpLabel,
            customerListViewLabel, customerOperationLabel, dateRangeHelpLabel;

    public CustomersSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateRoomComboBox();
        populateCustomerList();
        populateEventList();

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
        customerRefresh.setOnAction(e -> {
            populateRoomComboBox();
            populateCustomerList();
            resetCustomerForm();
            populateEventList();
        });
        eventReg.setOnAction(e -> registerEvent());
        cancelEventReg.setOnAction(e -> cancelEventRegistration());
    }

    private void populateEventList() {
        eventRegComboBox.setItems(FXCollections.observableList(eventRepo.findAll()));
    }
    //Button Actions - Customer Form
    private void addCustomer() {
        Room selectedRoom = customerRoomComboBox.getSelectionModel().getSelectedItem();
        String name = customerName.getText();
        String phone = customerPhone.getText();
        String email = customerEmail.getText();
        String state = customerState.getText();
        String country = customerCountry.getText();
        String extraInformation = extraInfo.getText();

        boolean isNameValid = validateName(name);
        boolean isPhoneValid = validatePhone(phone);
        boolean isEmailValid = validateEmail(email);
        boolean isStateValid = validateState(state);
        boolean isCountryValid = validateCountry(country);
        boolean isStartDateNotNull = Objects.nonNull(startDate.getValue());
        boolean isEndDateNotNull = Objects.nonNull(endDate.getValue());
        boolean isDateRangeValid =
                isStartDateNotNull
                        && isEndDateNotNull
                        && startDate.getValue().compareTo(endDate.getValue()) <= 0;

        if (isNameValid && isPhoneValid && isEmailValid && isStartDateNotNull && isEndDateNotNull && isDateRangeValid) {
            Date start = Date.from(startDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
            Date end = Date.from(endDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
            Customer customer;
            if(confirmCustomer.getText().equals("Save Changes")) {
                customer = customerListView.getSelectionModel().getSelectedItem();
                customer.setCustomerEmail(email);
                customer.setCustomerName(name);
                customer.setCustomerPhone(phone);
                // customer.setState(state) database
                // customer.setCountry(country) database
                customer.setExtraInfo(extraInformation);
                customer.setStartDate(start);
                customer.setEndDate(end);

                Room room = roomRepo.findById(customer.getRoomId()).orElse(null);

                if(Objects.nonNull(room)) {
                    room.setStatus(RoomStatus.VACANT);
                }
            } else {
                customer = new Customer(UUID.randomUUID(), name, phone, email, extraInformation, start, end);
            }
            customer.setRoom(selectedRoom.getRoomId());
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
            if(!isPhoneValid) {
                customerPhoneHelpLabel.setTextFill(Color.web("#F73331"));
                customerPhoneHelpLabel.setText("Invalid format: xxx-xxx-xxxx");
                customerPhoneHelpLabel.setVisible(true);
            } else {
                customerPhoneHelpLabel.setVisible(false);
            }
            if(!isStateValid) {
                customerStateHelpLabel.setTextFill(Color.web("#F73331"));
                customerStateHelpLabel.setText("Invalid format: State");
                customerStateHelpLabel.setVisible(true);
            } else {
                customerStateHelpLabel.setVisible(false);
            }
            if(!isCountryValid) {
                customerCountryHelpLabel.setTextFill(Color.web("#F73331"));
                customerCountryHelpLabel.setText("Invalid format: Country");
                customerCountryHelpLabel.setVisible(true);
            } else {
                customerCountryHelpLabel.setVisible(false);
            }
            if(!isEmailValid) {
                customerEmailHelpLabel.setTextFill(Color.web("#F73331"));
                customerEmailHelpLabel.setText("Invalid format: email@domain.com");
                customerEmailHelpLabel.setVisible(true);
            } else {
                customerPhoneHelpLabel.setVisible(false);
            }
            
            if(!isStartDateNotNull) {
                dateRangeHelpLabel.setTextFill(Color.web("#F73331"));
                dateRangeHelpLabel.setText("From Date cannot be null");
                dateRangeHelpLabel.setVisible(true);
            } else if(!isEndDateNotNull) {
                dateRangeHelpLabel.setTextFill(Color.web("#F73331"));
                dateRangeHelpLabel.setText("To date cannot be null");
                dateRangeHelpLabel.setVisible(true);
            } else if(!isDateRangeValid) {
                dateRangeHelpLabel.setTextFill(Color.web("#F73331"));
                dateRangeHelpLabel.setText("From date cannot be after To date");
                dateRangeHelpLabel.setVisible(true);
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
        // customerState.setText() database
        customerState.setEditable(false);
        // customerCountry.setText() database
        customerCountry.setEditable(false);
        customerEmail.setText(selectedCustomer.getCustomerEmail());
        customerEmail.setEditable(false);
        extraInfo.setText(selectedCustomer.getExtraInfo());
        extraInfo.setEditable(false);
        startDate.setValue(selectedCustomer.getStartDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
        startDate.setDisable(true);
        endDate.setValue(selectedCustomer.getEndDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
        endDate.setDisable(true);
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
        // customerState.setText() database
        // customerCountry.setText() database
        extraInfo.setText(selectedCustomer.getExtraInfo());
        startDate.setValue(selectedCustomer.getStartDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
        endDate.setValue(selectedCustomer.getEndDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
        confirmCustomer.setVisible(true);
        confirmCustomer.setText("Save Changes");
    }
    private void deleteCustomer() {
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
        if(Objects.nonNull(selectedCustomer)) {
            Room room = roomRepo.findById(selectedCustomer.getRoomId()).orElse(null);
            if(Objects.nonNull(room)) {
                room.setStatus(RoomStatus.VACANT);
                roomRepo.save(room);
            }
            customerRepo.delete(selectedCustomer);
        }
        resetCustomerForm();
        populateCustomerList();

    }

    //Util Methods - Customer Form
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
            customerListViewLabel.setText("");
            customerListViewLabel.setDisable(true);
        } else {
            customerListViewLabel.setDisable(false);
        }
        customerListView.setItems(customers.sorted());
    }
    private void resetCustomerForm() {
        customerOperationLabel.setText("Add Customer");
        customerRoomComboBox.getSelectionModel().clearSelection();
        customerRoomComboBox.setDisable(false);

        //Reset buttons
        confirmCustomer.setText("Add Customer");
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
        customerState.clear();
        customerState.setDisable(false);
        customerState.setEditable(true);
        customerCountry.clear();
        customerCountry.setDisable(false);
        customerCountry.setEditable(true);
        extraInfo.clear();
        extraInfo.setDisable(false);
        extraInfo.setEditable(true);
        startDate.setValue(null);
        startDate.setDisable(false);
        endDate.setValue(null);
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

    private void registerEvent() {
        Event event = eventRegComboBox.getSelectionModel().getSelectedItem();
            Customer customer = customerListView.getSelectionModel().getSelectedItem();
            customer.setEvent(event);
            customerRepo.save(customer);
    }

    private void cancelEventRegistration() {
        eventRegComboBox.getSelectionModel().clearSelection();
    }

    public AnchorPane getScene() {
        return pane;
    }
}
