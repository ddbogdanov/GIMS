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
import java.time.*;
import java.util.*;

@Component
@FxmlView("/EventsSceneController.fxml")
public class EventsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    EventRepo eventRepo;
    @Autowired
    LocationRepo locationRepo;
    @Autowired
    EventStatusRepo eventStatusRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    EventChargeRepo eventChargeRepo;
    @Autowired
    CustomerRepo customerRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Event> eventListView;
    @FXML private JFXListView<Location> locationListView;
    @FXML private JFXListView<Order> orderListView;
    @FXML private JFXListView<EventCharge> eventChargeListView;
    @FXML private JFXComboBox<Location> locationComboBox;
    @FXML private JFXComboBox<EventStatus> eventStatusComboBox;
    @FXML private JFXComboBox<Customer> customerComboBox;
    @FXML private JFXDatePicker eventStartDatePicker, eventEndDatePicker;
    @FXML private JFXTimePicker eventStartTimePicker, eventEndTimePicker;
    @FXML private JFXTextField eventNameTextField, locationNameTextField, capacityTextField, chargeAmountTextField;
    @FXML private JFXTextArea eventExtraInfoArea, locationInfoTextArea, chargeDescriptionTextArea;
    @FXML private Label
            eventFormLabel, locationFormLabel, eventFormHelpLabel, locationFormHelpLabel,
            chargeFormLabel, chargeOrderIdLabel, chargeFormHelpLabel, chargeAmountHelpLabel,
            chargeFormOrderIdLabel, chargeListLabel;
    @FXML private JFXButton
            eventFormSubmitButton, eventFormCancelButton, locationFormSubmitButton, locationFormCancelButton,
            editEventButton, viewEventButton, deleteEventButton, editLocationButton, viewLocationButton, deleteLocationButton,
            editChargeButton, deleteChargeButton, chargeFormSubmitButton, chargeFormCancelButton;

    public EventsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateEventListView();
        populateLocationListView();
        populateLocationComboBox();
        populateEventStatusComboBox();
        populateCustomerComboBox();

        //Event Form
        eventFormSubmitButton.setOnAction(e -> {
            submitEvent();
        });
        editEventButton.setOnAction(e -> {
            editEvent();
        });
        eventFormCancelButton.setOnAction(e -> {
            resetEventForm();
        });
        viewEventButton.setOnAction(e -> {
            viewEvent();
        });
        deleteEventButton.setOnAction(e -> {
            deleteEvent();
        });

        //Location Form
        locationFormSubmitButton.setOnAction(e -> {
            submitLocation();
        });
        editLocationButton.setOnAction(e -> {
            editLocation();
        });
        viewLocationButton.setOnAction(e -> {
            viewLocation();
        });
        deleteLocationButton.setOnAction(e -> {
            deleteLocation();
        });
        locationFormCancelButton.setOnAction(e -> {
            resetLocationForm();
        });

        //Charge Form
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

        chargeAmountTextField.focusedProperty().addListener((obs, oldVal, newVal) -> chargeAmountHelpLabel.setVisible(newVal));
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(customerComboBox.getValue() != null) {
                populateOrderListView();
                resetChargeForm();
                chargeListLabel.setText("Event Charges - {Order ID}");
                eventChargeListView.getItems().clear();
            }
        });
        orderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateChargeListView(newVal.getOrderId());
                resetChargeForm();
                chargeListLabel.setText("Event Charges - Order #" + newVal.getOrderId());
            }
        });
        eventChargeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == null) {
                chargeListLabel.setText("Event Charges - {Order ID}");
            }
        });
    }

    //Button Actions - Charge Form
    private void submitCharge() {
        if(validateChargeForm()) {
            Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
            if(selectedOrder != null) {
                EventCharge charge = new EventCharge(
                        selectedOrder,
                        chargeDescriptionTextArea.getText(),
                        new BigDecimal(chargeAmountTextField.getText()));
                eventChargeRepo.save(charge);
                updateOrderTotal(charge);
                populateOrderListView();
                populateChargeListView(selectedOrder.getOrderId());
                resetChargeForm();
            }
            else {
                System.err.println("Nothing selected in order list");
            }
        }
    }
    private void resetChargeForm() {
        chargeFormSubmitButton.setText("Add Event Charge");

        chargeAmountTextField.clear();
        chargeDescriptionTextArea.clear();

        chargeFormHelpLabel.setTextFill(Color.web("#5BDDC7"));
        chargeFormHelpLabel.setVisible(false);
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
        if(validateChargeForm()) {
            if(orderListView.getSelectionModel().getSelectedItem() != null) {
                BigDecimal oldCharge = eventChargeListView.getSelectionModel().getSelectedItem().getCharge();

                EventCharge charge = new EventCharge(
                        eventChargeListView.getSelectionModel().getSelectedItem().getChargeId(),
                        orderListView.getSelectionModel().getSelectedItem(),
                        chargeDescriptionTextArea.getText(),
                        new BigDecimal(chargeAmountTextField.getText()));
                eventChargeRepo.save(charge);

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
                populateOrderListView();
                populateChargeListView(eventChargeListView.getSelectionModel().getSelectedItem().getChargeId());
                resetChargeForm();
            }
            else {
                System.err.println("Nothing selected in order list to submit edit");
            }
        }
    }
    private void deleteCharge() {
        EventCharge selectedCharge = eventChargeListView.getSelectionModel().getSelectedItem();

        eventChargeRepo.delete(selectedCharge);

        selectedCharge.setCharge(selectedCharge.getCharge().multiply(new BigDecimal(-1)));
        updateOrderTotal(selectedCharge);

        populateOrderListView();
        populateChargeListView(selectedCharge.getOrderId());
        resetChargeForm();
    }

    //Util Methods - Charge Form
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
            chargeFormHelpLabel.setTextFill(Color.web("#F73331"));
            chargeFormHelpLabel.setText("Description cannot be blank");
            chargeFormHelpLabel.setVisible(true);
            return false;
        }
        return true;
    }
    private void updateOrderTotal(EventCharge charge) {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        Customer selectedCustomer = customerComboBox.getValue();

        BigDecimal oldTotal = selectedOrder.getTotal();
        BigDecimal newTotal = oldTotal.add(charge.getCharge());

        Order order = new Order(
                selectedOrder.getOrderId(),
                selectedCustomer,
                selectedOrder.getStay(),
                newTotal);

        orderRepo.save(order);
    }
    private void populateOrderListView() {
        Customer selectedCustomer = customerComboBox.getValue();
        ObservableList<Order> orders = FXCollections.observableArrayList();

        if(orderRepo.count() != 0) {
            orders.addAll(orderRepo.findAllByCustomer_CustomerId(selectedCustomer.getCustomerId()));
        }
        orderListView.setItems(orders.sorted());
    }
    private void populateChargeListView(int orderId) {
        chargeListLabel.setText("Event Charges - Order #" + orderId);
        ObservableList<EventCharge> charges = FXCollections.observableArrayList();

        if(eventChargeRepo.count() != 0) {
            charges.addAll(eventChargeRepo.findAllByOrder_OrderId(orderId));
        }
        eventChargeListView.setItems(charges);
    }
    private void populateCustomerComboBox() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        if(customerRepo.count() != 0) {
            customerList.addAll(customerRepo.findAll());
        }
        customerComboBox.setItems(customerList);
    }
    private void populateChargeForm() {
        EventCharge selectedCharge = eventChargeListView.getSelectionModel().getSelectedItem();

        chargeAmountTextField.setText(selectedCharge.getCharge().toString());
        chargeDescriptionTextArea.setText(selectedCharge.getDescription());
    }






    //Button Actions - Event Form
    private void submitEvent() {
        if(validateEventForm()) {
            String name = eventNameTextField.getText();
            Location location = locationComboBox.getValue();
            EventStatus eventStatus = eventStatusComboBox.getValue();
            LocalDateTime startDateTime = LocalDateTime.of(eventStartDatePicker.getValue(), eventStartTimePicker.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(eventEndDatePicker.getValue(), eventEndTimePicker.getValue());
            String extraInfo = eventExtraInfoArea.getText();

            Event event = new Event(
                    UUID.randomUUID(),
                    location,
                    eventStatus,
                    name,
                    extraInfo,
                    startDateTime,
                    endDateTime);
            eventRepo.save(event);

            populateEventListView();
            resetEventForm();
        }
    }
    private void editEvent() {
        resetEventForm();
        try {
            eventFormSubmitButton.setOnAction(e -> {
                submitEventEdits();
            });

            populateEventForm();
            eventFormLabel.setText("Edit an Event");
            eventFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            eventFormSubmitButton.setOnAction(e -> {
                submitEvent();
            });
            System.err.println("Nothing selected in events list");
        }
    }
    private void submitEventEdits() {
        if(validateEventForm()) {
            String name = eventNameTextField.getText();
            Location location = locationComboBox.getValue();
            EventStatus eventStatus = eventStatusComboBox.getValue();
            LocalDateTime startDateTime = LocalDateTime.of(eventStartDatePicker.getValue(), eventStartTimePicker.getValue());
            LocalDateTime endDateTime = LocalDateTime.of(eventEndDatePicker.getValue(), eventEndTimePicker.getValue());
            String extraInfo = eventExtraInfoArea.getText();

            Event updatedEvent = new Event(
                    eventListView.getSelectionModel().getSelectedItem().getEventId(),
                    location,
                    eventStatus,
                    name,
                    extraInfo,
                    startDateTime,
                    endDateTime);
            eventRepo.save(updatedEvent);

            populateEventListView();
            resetEventForm();
        }
    }
    private void viewEvent() {
        try {
            populateEventForm();

            locationComboBox.setDisable(true);
            eventStatusComboBox.setDisable(true);
            eventNameTextField.setDisable(true);
            eventStartDatePicker.setDisable(true);
            eventEndDatePicker.setDisable(true);
            eventStartTimePicker.setDisable(true);
            eventEndTimePicker.setDisable(true);
            eventFormSubmitButton.setDisable(true);
            eventExtraInfoArea.setDisable(true);
        }
        catch(NullPointerException ex) {
            System.err.println("Nothing selected in event list");
        }
    }
    private void deleteEvent() {
        try {
            Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
            eventRepo.delete(selectedEvent);

            populateEventListView();
            resetEventForm();
        }
        catch(NullPointerException ex) {
            System.err.println("Nothing selected in event list");
        }
    }
    private void resetEventForm() {
        eventFormLabel.setText("Add an Event");
        eventFormHelpLabel.setVisible(false);

        locationComboBox.getSelectionModel().clearSelection();
        locationComboBox.setDisable(false);
        eventStatusComboBox.getSelectionModel().clearSelection();
        eventStatusComboBox.setDisable(false);

        eventStartDatePicker.getEditor().clear();
        eventStartDatePicker.setValue(null);
        eventStartDatePicker.setDisable(false);
        eventStartTimePicker.getEditor().clear();
        eventStartTimePicker.setValue(null);
        eventStartTimePicker.setDisable(false);

        eventEndDatePicker.getEditor().clear();
        eventEndDatePicker.setValue(null);
        eventEndDatePicker.setDisable(false);
        eventEndTimePicker.getEditor().clear();
        eventEndTimePicker.setValue(null);
        eventEndTimePicker.setDisable(false);

        eventNameTextField.clear();
        eventNameTextField.setDisable(false);
        eventExtraInfoArea.clear();
        eventExtraInfoArea.setDisable(false);

        eventFormSubmitButton.setText("Add Event");
        eventFormSubmitButton.setDisable(false);

        eventFormSubmitButton.setOnAction(e -> {
            submitEvent();
        });
    }

    //Util Methods - Event Form
    private boolean validateEventForm() {
        if(eventNameTextField.getText().isBlank()) {
            eventFormHelpLabel.setText("Give the event a name");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(locationComboBox.getSelectionModel().isEmpty()) {
            eventFormHelpLabel.setText("Choose a location");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(eventStartDatePicker.getValue() == null) {
            eventFormHelpLabel.setText("Start date is not valid");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(eventStartTimePicker.getValue() == null) {
            eventFormHelpLabel.setText("Start time is not valid");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(eventEndDatePicker.getValue() == null) {
            eventFormHelpLabel.setText("End date is not valid");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(eventEndTimePicker.getValue() == null) {
            eventFormHelpLabel.setText("End time is not valid");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else if(eventExtraInfoArea.getText().isBlank()) {
            eventFormHelpLabel.setText("Provide extra information");
            eventFormHelpLabel.setVisible(true);
            return false;
        }

        LocalDateTime startDateTime = LocalDateTime.of(eventStartDatePicker.getValue(), eventEndTimePicker.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(eventEndDatePicker.getValue(), eventEndTimePicker.getValue());

        if(startDateTime.isAfter(endDateTime)) {
            eventFormHelpLabel.setText("Date range is not valid. Start Date is after End Date");
            eventFormHelpLabel.setVisible(true);
            return false;
        }
        else {
            return true;
        }
    }
    private void populateEventForm() {
        Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();

        for(Location location : locationComboBox.getItems()) {
            if(selectedEvent.getLocationId().equals(location.getLocationId())){
                locationComboBox.getSelectionModel().select(location);
                break;
            }
        }
        for(EventStatus eventStatus : eventStatusComboBox.getItems()) {
            if(selectedEvent.getEventStatusId() == eventStatus.getEventStatusId()) {
                eventStatusComboBox.getSelectionModel().select(eventStatus);
                break;
            }
        }

        eventNameTextField.setText(selectedEvent.getEventName());
        eventStartDatePicker.setValue(selectedEvent.getStartDateTime().toLocalDate());
        eventStartTimePicker.setValue(selectedEvent.getStartDateTime().toLocalTime());
        eventEndDatePicker.setValue(selectedEvent.getEndDateTime().toLocalDate());
        eventEndTimePicker.setValue(selectedEvent.getEndDateTime().toLocalTime());
        eventExtraInfoArea.setText(selectedEvent.getEventInfo());
    }
    private void populateLocationComboBox() {
        locationComboBox.getItems().clear();
        ObservableList<Location> locationList = FXCollections.observableArrayList();
        if(locationRepo.count() != 0) {
            locationList.addAll(locationRepo.findAll());
        }
        locationComboBox.setItems(locationList);
    }
    private void populateEventStatusComboBox() {
        eventStatusComboBox.getItems().clear();
        ObservableList<EventStatus> eventStatusList = FXCollections.observableArrayList();
        if(eventStatusRepo.count() != 0) {
            eventStatusList.addAll(eventStatusRepo.findAll());
        }
        eventStatusComboBox.setItems(eventStatusList);
    }
    private void populateEventListView() {
        ObservableList<Event> eventsList = FXCollections.observableArrayList();

        if(eventRepo.count() != 0) {
            eventsList.addAll(eventRepo.findAll());
        }
        eventListView.setItems(eventsList);
    }

    //Button Actions - Location Form
    private void submitLocation() {
        if(validateLocationForm()) {
            String name = locationNameTextField.getText();
            int capacity = Integer.parseInt(capacityTextField.getText());
            String info = locationInfoTextArea.getText();

            Location location = new Location(
                    UUID.randomUUID(),
                    name,
                    info,
                    capacity);
            locationRepo.save(location);

            populateLocationListView();
            populateLocationComboBox();
            resetLocationForm();
        }
    }
    private void editLocation() {
        resetLocationForm();
        try {
            locationFormSubmitButton.setOnAction(e -> {
                submitLocationEdits();
            });

            populateLocationForm();
            locationFormLabel.setText("Edit Location");
            locationFormSubmitButton.setText("Submit Edits");
        }
        catch(NullPointerException ex) {
            locationFormSubmitButton.setOnAction(e -> {
                submitLocation();
            });
            System.err.println("Nothing selected in location list");
        }
    }
    private void submitLocationEdits() {
        if(validateLocationForm()) {
            UUID locationId = locationListView.getSelectionModel().getSelectedItem().getLocationId();
            String name = locationNameTextField.getText();
            int capacity = Integer.parseInt(capacityTextField.getText());
            String info = locationInfoTextArea.getText();

            Location location = new Location(
                    locationId,
                    name,
                    info,
                    capacity);
            locationRepo.save(location);

            populateLocationListView();
            populateLocationComboBox();
            resetLocationForm();
        }
    }
    private void viewLocation() {
        try {
            populateLocationForm();

            locationFormLabel.setText("View Location");
            locationNameTextField.setDisable(true);
            capacityTextField.setDisable(true);
            locationInfoTextArea.setDisable(true);
            locationFormSubmitButton.setDisable(true);

        }
        catch(NullPointerException ex) {
            System.err.println("Nothing selected in location list");
        }
    }
    private void deleteLocation() {
        try {
            Location selectedLocation = locationListView.getSelectionModel().getSelectedItem();
            locationRepo.delete(selectedLocation);

            populateLocationListView();
            populateLocationComboBox();
            resetLocationForm();
        }
        catch(NullPointerException ex) {
            System.err.println("Nothing selected in location list");
        }
    }

    //Util Methods - Location Form
    private boolean validateLocationForm() {
        if(locationNameTextField.getText().isBlank()) {
            locationFormHelpLabel.setText("Set a location name");
            locationFormHelpLabel.setVisible(true);
            return false;
        }
        else if(capacityTextField.getText().isBlank()) {
            locationFormHelpLabel.setText("Set a capacity");
            locationFormHelpLabel.setVisible(true);
            return false;
        }
        else if(locationInfoTextArea.getText().isBlank()) {
            locationFormHelpLabel.setText("Set location information");
            locationFormHelpLabel.setVisible(true);
            return false;
        }
        else {
            try {
                Integer.parseInt(capacityTextField.getText());
            }
            catch(NumberFormatException ex) {
                locationFormHelpLabel.setText("Capacity is invalid. Type only whole numbers");
                locationFormHelpLabel.setVisible(true);
                return false;
            }
            return true;
        }
    }
    private void populateLocationForm() {
        Location selectedLocation = locationListView.getSelectionModel().getSelectedItem();

        locationNameTextField.setText(selectedLocation.getLocationName());
        capacityTextField.setText(String.valueOf(selectedLocation.getCapacity()));
        locationInfoTextArea.setText(selectedLocation.getLocationInfo());
    }
    private void populateLocationListView() {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        if(locationRepo.count() != 0) {
            locations.addAll(locationRepo.findAll());
        }
        int i = locationListView.getSelectionModel().getSelectedIndex();
        locationListView.setItems(locations);
        locationListView.getSelectionModel().select(i);
    }
    private void resetLocationForm() {
        locationFormLabel.setText("Add Location");

        locationNameTextField.clear();
        locationNameTextField.setDisable(false);
        capacityTextField.clear();
        capacityTextField.setDisable(false);
        locationInfoTextArea.clear();
        locationInfoTextArea.setDisable(false);

        locationFormSubmitButton.setText("Add Location");
        locationFormSubmitButton.setDisable(false);

        locationFormSubmitButton.setOnAction(e -> {
            submitLocation();
        });
    }

    public AnchorPane getScene() {
        return pane;
    }
}