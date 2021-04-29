package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.*;
import com.cougarneticit.gims.model.repos.*;
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

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private AdditionalChargeRepo additionalChargeRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXComboBox<Customer> customerComboBox;
    @FXML private JFXComboBox<Room> roomComboBox;
    @FXML private JFXComboBox<Stay> stayComboBox;
    @FXML private JFXListView<Customer> customerListView;
    @FXML private JFXListView<Stay> stayListView;
    @FXML private JFXListView<Order> orderListView;
    @FXML private JFXListView<AdditionalCharge> chargeListView;
    @FXML private JFXDatePicker stayStartDatePicker, stayEndDatePicker;
    @FXML private JFXTextField customerNameTextField, customerPhoneTextField, customerEmailTextField, chargeAmountTextField;
    @FXML private JFXTextArea chargeDescriptionTextArea;

    @FXML private Label
            customerNameHelpLabel, customerPhoneHelpLabel, customerEmailHelpLabel, customerFormLabel,
            stayListLabel, stayFormLabel, customerNameLabel, orderListLabel, chargeListLabel, orderTotalLabel,
            orderFormCustomerNameLabel, orderFormLabel, chargeAmountHelpLabel, chargeFormLabel, chargeFormOrderIdLabel;

    @FXML private JFXButton
            customerFormSubmitButton, customerFormCancelButton, viewCustomerButton, editCustomerButton, deleteCustomerButton,
            stayFormSubmitButton, stayFormCancelButton, editStayButton, viewStayButton, deleteStayButton, deleteOrderButton,
            deleteChargeButton, orderFormSubmitButton, orderFormCancelButton, chargeFormSubmitButton, chargeFormCancelButton,
            editOrderButton, editChargeButton;

    public CustomersSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateCustomerListView();
        customerListView.getSelectionModel().select(0);
        try {
            populateStayListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            populateOrderListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            populateStayComboBox(customerListView.getSelectionModel().getSelectedItem().getCustomerId());
            populateCustomerComboBox();
            populateRoomComboBox();

            orderListLabel.setText("Orders - " + customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            customerNameLabel.setText(customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            orderFormCustomerNameLabel.setText(customerListView.getSelectionModel().getSelectedItem().getCustomerName());
        }
        catch(NullPointerException ex) {
            System.err.println("No customers");
        }

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

        //Order form
        orderFormSubmitButton.setOnAction(e -> {
            submitOrder();
        });
        orderFormCancelButton.setOnAction(e -> {
            resetOrderForm();
        });
        editOrderButton.setOnAction(e -> {
            editOrder();
        });
        deleteOrderButton.setOnAction(e -> {
            deleteOrder();
        });

        //Charge form
        chargeFormSubmitButton.setOnAction(e -> {
            submitCharge();
        });
        chargeFormCancelButton.setOnAction(e -> {
            resetChargeForm();
        });
        editChargeButton.setOnAction(e -> {
            editCharge();
        });
        deleteChargeButton.setOnAction(e -> {
            deleteCharge();
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

        chargeAmountTextField.focusedProperty().addListener((obs, oldVal, newVal) -> chargeAmountHelpLabel.setVisible(newVal));
        customerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                customerListView.getSelectionModel().select(oldVal);
            }
            else {
                populateStayListView(newVal.getCustomerId(), newVal.getCustomerName());
                populateStayComboBox(newVal.getCustomerId());
                populateOrderListView(newVal.getCustomerId(), newVal.getCustomerName());

                resetOrderForm();
                resetChargeForm();
                customerNameLabel.setText(newVal.getCustomerName());
                orderFormCustomerNameLabel.setText(newVal.getCustomerName());
                chargeListLabel.setText("Additional Charges - {Order ID}");
                orderListView.getSelectionModel().clearSelection();
                chargeListView.getItems().clear();
            }
        });
        orderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                //orderListView.getSelectionModel().select(oldVal);
            }
            else {
                populateChargeListView(newVal.getOrderId());

                resetOrderForm();
                resetChargeForm();
                orderTotalLabel.setText("$" + newVal.getTotal().toString());
                chargeListLabel.setText("Additional Charges - Order #" + newVal.getOrderId());
                chargeFormOrderIdLabel.setText("Order #" + newVal.getOrderId());
            }
        });
        chargeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                chargeListView.getSelectionModel().select(oldVal);
            }
            else {
                resetChargeForm();
                chargeFormOrderIdLabel.setText("Order #" + newVal.getOrderId());
            }
        });
        chargeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                chargeListLabel.setText("Event Charges - {Order ID}");
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

    //TODO: Update labels after delete customer
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

    //Button Actions - Order Form
    private void submitOrder() {
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        Order order = createOrder(selectedCustomer);
        orderRepo.save(order);

        populateOrderListView(selectedCustomer.getCustomerId(), selectedCustomer.getCustomerName());
        resetOrderForm();
    }
    private void resetOrderForm() {
        stayComboBox.setDisable(false);
        stayComboBox.getSelectionModel().clearSelection();

        orderFormLabel.setText("Create Order");
        orderTotalLabel.setText("-");

        orderFormSubmitButton.setText("Create");

        orderFormSubmitButton.setOnAction(e -> {
            submitOrder();
        });
    }
    private void editOrder() {
        resetOrderForm();
        try {
            orderFormSubmitButton.setOnAction(e -> {
                submitOrderEdits();
            });

            populateOrderForm();
            orderFormLabel.setText("Edit Order");
            orderFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            orderFormSubmitButton.setOnAction(e -> {
                submitOrder();
            });
            System.err.println("Nothing selected order list");
        }
    }
    private void submitOrderEdits() {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        Order editedOrder = createOrder(selectedOrder.getCustomer());
        editedOrder.setOrderId(selectedOrder.getOrderId());

        Set<AdditionalCharge> addCharges = selectedOrder.getAdditionalCharges();
        Set<EventCharge> eventCharges = selectedOrder.getEventCharges();
        addAdditionalCharges(editedOrder, addCharges, eventCharges);

        orderRepo.save(editedOrder);

        populateOrderListView(selectedOrder.getCustomer().getCustomerId(), selectedOrder.getCustomer().getCustomerName());
        resetOrderForm();
    }
    private void deleteOrder() {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();

        orderRepo.delete(selectedOrder);

        populateOrderListView(selectedOrder.getCustomer().getCustomerId(), selectedOrder.getCustomer().getCustomerName());
        resetOrderForm();
    }
    //Util Methods - Order Form
    private void populateOrderListView(UUID customerId, String customerName) {
        orderListLabel.setText("Orders: " + customerName);
        ObservableList<Order> orders = FXCollections.observableArrayList();

        if(orderRepo.count() != 0) {
            orders.addAll(orderRepo.findAllByCustomer_CustomerId(customerId));
        }
        orderListView.setItems(orders.sorted());
    }
    private void populateStayComboBox(UUID customerId) {
        stayComboBox.getItems().clear();
        ObservableList<Stay> stayList = FXCollections.observableArrayList();
        if(stayRepo.count() != 0) {
            stayList.addAll(stayRepo.findAllByCustomer_CustomerId(customerId));
        }
        stayComboBox.setItems(stayList);
    }
    private void populateOrderForm() {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();

       if(selectedOrder.getStay() != null) {
           try {
               for (Stay stay : stayComboBox.getItems()) {
                   if (selectedOrder.getStayId() == stay.getStayId()) {
                       stayComboBox.getSelectionModel().select(stay);
                       break;
                   }
               }
           } catch (NullPointerException ex) {
               ex.printStackTrace();
               System.err.println("Selected order does not have a stay assigned");
           }
       }
       orderTotalLabel.setText("$" + selectedOrder.getTotal().toString());

    }
    private Order createOrder(Customer customer) {
        Stay selectedStay;
        BigDecimal orderTotal;

        try {
            selectedStay = stayComboBox.getValue();
            long daysDuration = ChronoUnit.DAYS.between(selectedStay.getStartDate(), selectedStay.getEndDate());
            orderTotal = selectedStay.getRoom().getRate().multiply(new BigDecimal(daysDuration));
        }
        catch(NullPointerException ex) {
            selectedStay = null;
            orderTotal = new BigDecimal(0);
        }

        return new Order(customer, selectedStay, orderTotal);
    }

    //Button Actions - Charge Form
    private void submitCharge() {
        if(validateChargeForm()) {
            Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
            if(selectedOrder != null) {
                AdditionalCharge charge = new AdditionalCharge(
                        selectedOrder,
                        chargeDescriptionTextArea.getText(),
                        new BigDecimal(chargeAmountTextField.getText()));
                additionalChargeRepo.save(charge);
                updateOrderTotal(charge);
                populateOrderListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
                populateChargeListView(selectedOrder.getOrderId());
                resetChargeForm();
            }
            else {
                System.err.println("Nothing selected in order list");
            }
        }
    }
    private void resetChargeForm() {
        chargeFormSubmitButton.setText("Add");

        chargeAmountTextField.clear();
        chargeDescriptionTextArea.clear();

        chargeAmountHelpLabel.setTextFill(Color.web("#5BDDC7"));
        chargeAmountHelpLabel.setText("####.##");
        chargeAmountHelpLabel.setVisible(false);
        chargeFormOrderIdLabel.setText("Order #--");
        chargeFormLabel.setText("Add Charge");

        chargeFormSubmitButton.setOnAction(e -> {
            submitCharge();
        });
    }
    private void editCharge() {
        resetChargeForm();
        try {
            chargeFormSubmitButton.setOnAction(e -> {
                submitChargeEdits();
            });

            populateChargeForm();
            chargeFormLabel.setText("Edit a Charge");
            chargeFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            chargeFormSubmitButton.setOnAction(e -> {
                submitCharge();
            });
            System.err.println("Nothing selected in charges list");
        }
    }
    private void submitChargeEdits() {
        if (validateChargeForm()) {
            if(orderListView.getSelectionModel().getSelectedItem() != null) {
                BigDecimal oldCharge = chargeListView.getSelectionModel().getSelectedItem().getCharge();

                AdditionalCharge charge = new AdditionalCharge(
                        chargeListView.getSelectionModel().getSelectedItem().getChargeId(),
                        orderListView.getSelectionModel().getSelectedItem(),
                        chargeDescriptionTextArea.getText(),
                        new BigDecimal(chargeAmountTextField.getText()));
                additionalChargeRepo.save(charge);

                if (charge.getCharge().compareTo(oldCharge) < 0) {
                    charge.setCharge(oldCharge.subtract(charge.getCharge()).multiply(new BigDecimal(-1)));
                }
                else if (charge.getCharge().compareTo(oldCharge) > 0) {
                    charge.setCharge(charge.getCharge().subtract(oldCharge));
                }
                else {
                    charge.setCharge(new BigDecimal(0));
                }

                updateOrderTotal(charge); //TODO calculate order total in order object
                populateOrderListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
                populateChargeListView(chargeListView.getSelectionModel().getSelectedItem().getChargeId());
                resetChargeForm();
            }
            else {
                System.err.println("Nothing selected in order list to submit edit");
            }
        }
    }
    private void deleteCharge() {
        AdditionalCharge selectedCharge = chargeListView.getSelectionModel().getSelectedItem();

        if(selectedCharge != null) {
            additionalChargeRepo.delete(selectedCharge);

            selectedCharge.setCharge(selectedCharge.getCharge().multiply(new BigDecimal(-1)));
            updateOrderTotal(selectedCharge);

            populateOrderListView(customerListView.getSelectionModel().getSelectedItem().getCustomerId(), customerListView.getSelectionModel().getSelectedItem().getCustomerName());
            populateChargeListView(selectedCharge.getOrderId());
            resetChargeForm();
        }
    }
    //Util Methods - Charge Form
    private void populateChargeListView(int orderId) {
        chargeListLabel.setText("Additional Charges - Order #" + orderId);
        ObservableList<AdditionalCharge> charges = FXCollections.observableArrayList();

        if(additionalChargeRepo.count() != 0) {
            charges.addAll(additionalChargeRepo.findAllByOrder_OrderId(orderId));
        }
        chargeListView.setItems(charges);
    }
    private void populateChargeForm() {
        AdditionalCharge selectedCharge = chargeListView.getSelectionModel().getSelectedItem();

        chargeAmountTextField.setText(selectedCharge.getCharge().toString());
        chargeDescriptionTextArea.setText(selectedCharge.getDescription());
    }
    private void updateOrderTotal(AdditionalCharge charge) {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();

        if(selectedOrder != null) {
            BigDecimal oldTotal = selectedOrder.getTotal();
            BigDecimal newTotal = oldTotal.add(charge.getCharge());

            Order order = new Order(
                    selectedOrder.getOrderId(),
                    selectedCustomer,
                    selectedOrder.getStay(),
                    newTotal);

            orderRepo.save(order);
        }
    }
    private Order addAdditionalCharges(Order editedOrder, Set<AdditionalCharge> addCharges, Set<EventCharge> eventCharges) {
        BigDecimal newTotal = new BigDecimal(0);

        if(!addCharges.isEmpty()) {
            for (AdditionalCharge charge : addCharges) {
                newTotal = newTotal.add(charge.getCharge());
            }
        }
        if(!eventCharges.isEmpty()) {
            for (EventCharge charge : eventCharges) {
                newTotal = newTotal.add(charge.getCharge());
            }
        }

        editedOrder.addToTotal(newTotal);
        return editedOrder;
    }
    private boolean validateChargeForm() {
        try {
            BigDecimal d = new BigDecimal(chargeAmountTextField.getText());
        }
        catch(NumberFormatException ex) {
            chargeAmountHelpLabel.setTextFill(Color.web("#F73331"));
            chargeAmountHelpLabel.setText("Invalid format: ####.##");
            chargeAmountHelpLabel.setVisible(true);
            return false;
        }

        if(chargeDescriptionTextArea.getText().isBlank()) {
            return false;
        }
        return true;
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
            populateStayComboBox(customerListView.getSelectionModel().getSelectedItem().getCustomerId());
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
            populateStayComboBox(customerListView.getSelectionModel().getSelectedItem().getCustomerId());
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
