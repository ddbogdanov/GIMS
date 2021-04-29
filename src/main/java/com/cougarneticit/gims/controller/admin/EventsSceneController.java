package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Event;
import com.cougarneticit.gims.model.EventStatus;
import com.cougarneticit.gims.model.Location;
import com.cougarneticit.gims.model.repos.EventRepo;
import com.cougarneticit.gims.model.repos.EventStatusRepo;
import com.cougarneticit.gims.model.repos.LocationRepo;
import com.jfoenix.controls.*;
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

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Event> eventListView;
    @FXML private JFXListView<Location> locationListView;
    @FXML private JFXComboBox<Location> locationComboBox;
    @FXML private JFXComboBox<EventStatus> eventStatusComboBox;
    @FXML private JFXDatePicker eventStartDatePicker, eventEndDatePicker;
    @FXML private JFXTimePicker eventStartTimePicker, eventEndTimePicker;
    @FXML private JFXTextField eventNameTextField, locationNameTextField, capacityTextField;
    @FXML private JFXTextArea eventExtraInfoArea, locationInfoTextArea;
    @FXML private Label eventFormLabel, locationFormLabel, eventFormHelpLabel, locationFormHelpLabel;
    @FXML private JFXButton
            eventFormSubmitButton, eventFormCancelButton, locationFormSubmitButton, locationFormCancelButton,
            editEventButton, viewEventButton, deleteEventButton, editLocationButton, viewLocationButton, deleteLocationButton;

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
        eventEndDatePicker.setValue(selectedEvent.getStartDateTime().toLocalDate());
        eventEndTimePicker.setValue(selectedEvent.getStartDateTime().toLocalTime());
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