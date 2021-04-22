package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Customer;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.Stay;
import com.cougarneticit.gims.model.repos.CustomerRepo;
import com.cougarneticit.gims.model.repos.RoomRepo;
import com.cougarneticit.gims.model.repos.StayRepo;
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
import java.util.*;

@Component
@FxmlView("/CustomersSceneController.fxml")
public class CustomersSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private StayRepo stayRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<Customer> customerComboBox;
    @FXML private JFXComboBox<Room> roomComboBox;
    @FXML private JFXListView<Customer> customerListView;
    @FXML private JFXListView<Stay> stayListView;
    @FXML private JFXDatePicker stayStartDatePicker, stayEndDatePicker;
    @FXML private JFXTextField customerNameTextField, customerPhoneTextField, customerEmailTextField;

    @FXML private Label
            customerNameHelpLabel, customerPhoneHelpLabel, customerEmailHelpLabel, customerFormLabel,
            stayListLabel, stayFormLabel, customerNameLabel;

    @FXML private JFXButton
            customerFormSubmitButton, customerFormCancelButton, viewCustomerButton, editCustomerButton, deleteCustomerButton,
            stayFormSubmitButton, stayFormCancelButton, editStayButton, viewStayButton, deleteStayButton;

    public CustomersSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateCustomerListView();
        customerListView.getSelectionModel().select(0);
        populateStayListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
        populateCustomerComboBox();
        populateRoomComboBox();

        //Customer form
        customerFormSubmitButton.setOnAction(e -> {
            submitCustomer();
        });
        customerFormCancelButton.setOnAction(e -> {
            resetCustomerForm();
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
        //Stay form
        stayFormSubmitButton.setOnAction(e -> {
            submitStay();
        });
        stayFormCancelButton.setOnAction(e -> {
            resetStayForm();
        });
        editStayButton.setOnAction(e -> {
            editStay();
        });
        viewStayButton.setOnAction(e -> {
            viewStay();
        });
        deleteStayButton.setOnAction(e -> {
            deleteStay();
        });

        customerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                customerListView.getSelectionModel().select(oldVal);
            }
            else {
                populateStayListView(newVal.getCustomerId(), newVal.getCustomerName());
                customerNameLabel.setText(newVal.getCustomerName());
            }
        });
        stayStartDatePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                stayStartDatePicker.getEditor().setText(stayStartDatePicker.getConverter().toString(stayStartDatePicker.getValue()));
            }
        });
        stayEndDatePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //Lazy workaround to unwanted behavior produced by the DatePicker
            if(newVal != null) {
                stayEndDatePicker.getEditor().setText(stayEndDatePicker.getConverter().toString(stayEndDatePicker.getValue()));
            }
        });
    }

    //Button Actions - Customer Form
    private void submitCustomer() {
        String name = customerNameTextField.getText();
        String phone = customerPhoneTextField.getText();
        String email = customerEmailTextField.getText();

        if(validateCustomerForm(name, phone, email)) {
            Customer customer = new Customer(UUID.randomUUID(), name, phone, email);

            customerRepo.save(customer);

            populateCustomerListView();
            resetCustomerForm();
        }
    }
    private void resetCustomerForm() {
        customerFormLabel.setText("Add a Customer");

        //Reset buttons
        customerFormSubmitButton.setText("Add Customer");
        customerFormSubmitButton.setDisable(false);
        customerFormCancelButton.setText("Cancel");
        //Reset text fields
        customerNameTextField.clear();
        customerNameTextField.setDisable(false);
        customerNameTextField.setEditable(true);
        customerPhoneTextField.clear();
        customerPhoneTextField.setDisable(false);
        customerPhoneTextField.setEditable(true);
        customerEmailTextField.clear();
        customerEmailTextField.setDisable(false);
        customerEmailTextField.setEditable(true);
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

        customerFormSubmitButton.setOnAction(e -> {
            submitCustomer();
        });

    }
    private void viewCustomer() {
        customerFormLabel.setText("View a Customer");
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        customerNameTextField.setText(selectedCustomer.getCustomerName());
        customerNameTextField.setEditable(false);
        customerPhoneTextField.setText(selectedCustomer.getCustomerPhone());
        customerPhoneTextField.setEditable(false);
        customerEmailTextField.setText(selectedCustomer.getCustomerEmail());
        customerEmailTextField.setEditable(false);
        customerFormSubmitButton.setDisable(true);
    }
    private void editCustomer() {
        resetCustomerForm();
        try {
            customerFormSubmitButton.setOnAction(e -> {
                submitCustomerEdits();
            });

            populateCustomerForm();
            customerFormLabel.setText("Edit a Customer");
            customerFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            customerFormSubmitButton.setOnAction(e -> {
                submitCustomer();
            });
            System.err.println("Nothing selected in customer list");
        }
    }
    private void submitCustomerEdits() {
        String name = customerNameTextField.getText();
        String phone = customerPhoneTextField.getText();
        String email = customerEmailTextField.getText();

        if(validateCustomerForm(name, phone, email)) {
            Customer updatedCustomer = new Customer(
                    customerListView.getSelectionModel().getSelectedItem().getCustomerId(),
                    name, phone, email);
            customerRepo.save(updatedCustomer);

            populateCustomerListView();
            populateCustomerComboBox();
            resetCustomerForm();
        }
    }
    private void deleteCustomer() {
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        customerRepo.delete(selectedCustomer);

        resetCustomerForm();
        populateCustomerListView();
    }

    //Util Methods - Customer Form
    private void populateCustomerListView() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        if(customerRepo.count() != 0) {
            customers.addAll(customerRepo.findAll());
        }
        customerListView.setItems(customers.sorted());
    }
    private void populateCustomerForm() {
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        customerNameTextField.setText(selectedCustomer.getCustomerName());
        customerPhoneTextField.setText(selectedCustomer.getCustomerPhone());
        customerEmailTextField.setText(selectedCustomer.getCustomerEmail());
    }
    private boolean validateCustomerForm(String name, String phone, String email) {
        boolean isNameValid = validateName(name);
        boolean isPhoneValid = validatePhone(phone);
        boolean isEmailValid = validateEmail(email);

        if(!isNameValid) {
            customerNameHelpLabel.setTextFill(Color.web("#F73331"));
            customerNameHelpLabel.setText("Invalid format: First Last");
            customerNameHelpLabel.setVisible(true);
            return false;
        }
        else if(!isPhoneValid) {
            customerPhoneHelpLabel.setTextFill(Color.web("#F73331"));
            customerPhoneHelpLabel.setText("Invalid format: xxx-xxx-xxxx");
            customerPhoneHelpLabel.setVisible(true);
            return false;
        }
        else if(!isEmailValid) {
            customerEmailHelpLabel.setTextFill(Color.web("#F73331"));
            customerEmailHelpLabel.setText("Invalid format: email@domain.com");
            customerEmailHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }

    }

    //Button Actions - Stay Form
    private void submitStay() {
        if(validateStayForm()) {
            LocalDate startDate = stayStartDatePicker.getValue();
            LocalDate endDate = stayEndDatePicker.getValue();

            Stay stay = new Stay(
                    customerComboBox.getValue(),
                    roomComboBox.getValue(),
                    startDate, endDate);
            stayRepo.save(stay);

            populateStayListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            resetStayForm();
        }
    }
    private void resetStayForm() {
        customerComboBox.getSelectionModel().clearSelection();
        customerComboBox.setDisable(false);
        roomComboBox.getSelectionModel().clearSelection();
        roomComboBox.setDisable(false);

        stayStartDatePicker.getEditor().clear();
        stayStartDatePicker.setDisable(false);
        stayEndDatePicker.getEditor().clear();
        stayEndDatePicker.setDisable(false);

        stayFormSubmitButton.setText("Add Stay");
        stayFormSubmitButton.setDisable(false);

        stayFormLabel.setText("Add a Stay");

        stayFormSubmitButton.setOnAction(e -> {
            submitStay();
        });
    }
    private void editStay() {
        resetStayForm();
        try {
            stayFormSubmitButton.setOnAction(e -> {
                submitStayEdits();
            });

            populateStayForm();
            stayFormLabel.setText("Edit a Stay");
            stayFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            stayFormSubmitButton.setOnAction(e -> {
                submitStay();
            });
            System.err.println("Nothing selected in stay list");
        }
    }
    private void submitStayEdits() {
        if(validateStayForm()) {
            Stay stay = new Stay(
                    stayListView.getSelectionModel().getSelectedItem().getStayId(),
                    customerComboBox.getValue(),
                    roomComboBox.getValue(),
                    stayStartDatePicker.getValue(),
                    stayEndDatePicker.getValue());
            stayRepo.save(stay);

            populateStayListView(stayListView.getSelectionModel().getSelectedItem().getCustomerId(), stayListView.getSelectionModel().getSelectedItem().getCustomerName());
            resetStayForm();
        }
    }
    private void viewStay() {
        resetStayForm();

        customerComboBox.setDisable(true);
        roomComboBox.setDisable(true);
        stayStartDatePicker.setDisable(true);
        stayEndDatePicker.setDisable(true);
        stayFormSubmitButton.setDisable(true);

        populateStayForm();
    }
    private void deleteStay() {
        Stay selectedStay = stayListView.getSelectionModel().getSelectedItem();

        stayRepo.delete(selectedStay);

        resetStayForm();
        populateStayListView(selectedStay.getCustomerId(), selectedStay.getCustomerName());
    }
    //Util Methods - Stay Form
    private void populateStayForm() {
        Stay selectedStay = stayListView.getSelectionModel().getSelectedItem();

        for(Customer customer : customerComboBox.getItems()) {
            if(selectedStay.getCustomerId().equals(customer.getCustomerId())) {
                customerComboBox.getSelectionModel().select(customer);
                customerComboBox.setDisable(true);
                break;
            }
        }
        for(Room room : roomComboBox.getItems()) {
            if(selectedStay.getRoomId() == room.getRoomId()) {
                roomComboBox.getSelectionModel().select(room);
                break;
            }
        }

        LocalDate startDate = selectedStay.getStartDate();
        LocalDate endDate = selectedStay.getEndDate();

        stayStartDatePicker.setValue(startDate);
        stayEndDatePicker.setValue(endDate);
    }
    private void populateStayListView(UUID customerId, String customerName) {
        stayListLabel.setText("Stay List - " + customerName);
        ObservableList<Stay> stays = FXCollections.observableArrayList();

        if(stayRepo.count() != 0) {
            stays.addAll(stayRepo.findAllByCustomer_CustomerId(customerId));
        }
        stayListView.setItems(stays.sorted());
    }
    private void populateCustomerComboBox() {
        customerComboBox.getItems().clear();
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        if(customerRepo.count() != 0) {
            customerList.addAll(customerRepo.findAll());
        }
        customerComboBox.setItems(customerList);
    }
    private void populateRoomComboBox() {
        roomComboBox.getItems().clear();
        ObservableList<Room> roomList = FXCollections.observableArrayList();
        if(roomRepo.count() != 0) {
            roomList.addAll(roomRepo.findAll());
        }
        roomComboBox.setItems(roomList);
    }
    private boolean validateStayForm() {
        if(customerComboBox.getSelectionModel().isEmpty()) {
            return false;
        }
        else if(roomComboBox.getSelectionModel().isEmpty()) {
            return false;
        }
        else if(stayStartDatePicker.getValue() == null) {
            return false;
        }
        else if(stayEndDatePicker.getValue() == null) {
            return false;
        }
        else {
            return true;
        }
    }
    public AnchorPane getScene() {
        return pane;
    }
}
