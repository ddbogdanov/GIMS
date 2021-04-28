package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Event;
import com.cougarneticit.gims.model.Location;
import com.cougarneticit.gims.model.repos.EventRepo;
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
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

@Component
@FxmlView("/EventsSceneController.fxml")
public class EventsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @Autowired
    EventRepo eventRepo;
    @Autowired
    LocationRepo locationRepo;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Event> eventsListView;
    @FXML private JFXListView<Location> locationListView;
    @FXML private JFXComboBox<Location> locationComboBox;
    @FXML private JFXDatePicker eventStartDatePicker, eventEndDatePicker;
    @FXML private JFXTimePicker eventStartTimePicker, eventEndTimePicker;
    @FXML private JFXTextField eventNameTextField, locationNameField, locationAudienceCapacity;
    @FXML private JFXTextArea eventExtraInfo, locationInformation;
    @FXML private Label eventFormLabel, locationFormLabel, eventNameHelpLabel, eventDateRangeHelpLabel;
    @FXML private JFXButton
            saveEventButton, cancelEventButton, addLocationButton, cancelLocationButton,
            editEventButton, viewEventButton, deleteEventButton, editLocationButton, viewLocationButton, deleteLocationButton;

    public EventsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateEvents();
        populateLocations();

        //Event Form
        saveEventButton.setOnAction(e -> {
            submitEvent();
        });
        editEventButton.setOnAction(e -> {
            editEvent();
        });
        cancelEventButton.setOnAction(e -> {
            resetEventForm();
        });
        viewEventButton.setOnAction(e -> {
            viewEvent();
        });
        deleteEventButton.setOnAction(e -> {
            deleteEvent();
        });
        //Location Form
        addLocationButton.setOnAction(e -> {
            addLocation();
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
        cancelLocationButton.setOnAction(e -> {
            resetLocationForm();
        });
    }

    //Button Actions - Event Form
    private void submitEvent() {
        String name = eventNameTextField.getText();
        Location location = locationComboBox.getValue();

        LocalDateTime startDateTime = LocalDateTime.of(eventStartDatePicker.getValue(), eventStartTimePicker.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(eventEndDatePicker.getValue(), eventEndTimePicker.getValue());

        String extraInfo = eventExtraInfo.getText();

        if(validateEventForm(name, location, startDateTime, endDateTime)) {

            /*if ("Add Event".equals(saveEventButton.getText())) {

                eventRepo.save(new Event(name, extraInfo,  location));

            }
            else {

                Event event = eventsListView.getSelectionModel().getSelectedItem();
                event.setEventName(name);
                event.setEventInfo(extraInfo);
                event.setStartDateTime(dateMaker.apply(startDate, startTime));
                event.setEndDateTime(dateMaker.apply(endDate, endTime));
                event.setLocation(location);
                eventRepo.save(event);

            }*/
        }

        populateEvents();
    }
    private void editEvent() {
        eventFormLabel.setText("Edit Event");
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        locationComboBox.getSelectionModel().select(event.getLocation());
        eventNameTextField.setText(event.getEventName());

        OffsetDateTime offsetDateTime = event.getStartDateTime().now().atStartOfDay().atOffset(ZoneOffset.UTC);
        eventStartDatePicker.setValue(offsetDateTime.toLocalDate());
        eventStartTimePicker.setValue(offsetDateTime.toLocalTime());
        offsetDateTime = event.getEndDateTime().now().atStartOfDay().atOffset(ZoneOffset.UTC);
        eventEndDatePicker.setValue(offsetDateTime.toLocalDate());
        eventEndTimePicker.setValue(offsetDateTime.toLocalTime());
        eventExtraInfo.setText(event.getEventInfo());
        saveEventButton.setText("Edit Event");
    }
    private void viewEvent() {
        resetEventForm();
        eventFormLabel.setText("View Event");
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        locationComboBox.getSelectionModel().select(event.getLocation());
        locationComboBox.setDisable(true);
        eventNameTextField.setText(event.getEventName());
        eventNameTextField.setEditable(false);
        OffsetDateTime offsetDateTime = event.getStartDateTime().now().atStartOfDay().atOffset(ZoneOffset.UTC);
        eventStartDatePicker.setValue(offsetDateTime.toLocalDate());
        eventStartDatePicker.setDisable(true);
        eventStartTimePicker.setValue(offsetDateTime.toLocalTime());
        eventStartTimePicker.setDisable(true);
        offsetDateTime = event.getEndDateTime().now().atStartOfDay().atOffset(ZoneOffset.UTC);
        eventEndDatePicker.setValue(offsetDateTime.toLocalDate());
        eventEndDatePicker.setDisable(true);
        eventEndTimePicker.setValue(offsetDateTime.toLocalTime());
        eventEndTimePicker.setDisable(true);
        eventExtraInfo.setText(event.getEventInfo());
        eventExtraInfo.setEditable(false);
        saveEventButton.setVisible(false);
    }
    private void deleteEvent() {
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        eventRepo.delete(event);
    }

    //Util Methods - Event Form
    private void resetEventForm() {
        eventFormLabel.setText("Add Event");
        eventNameHelpLabel.setText("");
        eventNameHelpLabel.setDisable(true);
        eventDateRangeHelpLabel.setText("");
        eventDateRangeHelpLabel.setDisable(true);
        saveEventButton.setVisible(true);
        saveEventButton.setText("Add Event");
        locationComboBox.setDisable(false);
        eventNameTextField.setEditable(true);
        eventStartDatePicker.setDisable(false);
        eventStartTimePicker.setDisable(false);
        eventEndDatePicker.setDisable(false);
        eventEndTimePicker.setDisable(false);
        eventExtraInfo.setEditable(true);
        locationComboBox.getSelectionModel().clearSelection();
        eventNameTextField.clear();
        eventStartDatePicker.setValue(null);
        eventStartTimePicker.setValue(null);
        eventEndDatePicker.setValue(null);
        eventEndTimePicker.setValue(null);
        //eventCustomerListView.getSelectionModel().clearSelection(); always null
        eventExtraInfo.clear();
    }
    private boolean validateEventForm(String name, Location location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        boolean isNameValid = Objects.nonNull(name);
        boolean isLocationValid = Objects.nonNull(location);
        boolean isStartDateValid = Objects.nonNull(startDate);
        boolean isStartTimeValid = Objects.nonNull(startTime);
        boolean isEndDateValid = Objects.nonNull(endDate);
        boolean isEndTimeValid = Objects.nonNull(endTime);
        boolean isDateRangeValid =
                isStartDateValid && isStartTimeValid
                        && isEndDateValid && isEndTimeValid
                        && ( startDate.compareTo(endDate) == 0
                        ? startTime.compareTo(endTime) < 0
                        : startDate.compareTo(endDate) < 0);

        if(!isNameValid) {
            eventNameHelpLabel.setText("Event Name should contain characters");
            eventNameHelpLabel.setDisable(false);
            return false;
        }
        else if(!isLocationValid) {
            return false;
        }
        else if(!isStartDateValid) {
            eventDateRangeHelpLabel.setText("Start Date cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
            return false;
        }
        else if(!isStartTimeValid) {
            eventDateRangeHelpLabel.setText("Start Time cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
            return false;
        }
        else if(!isEndDateValid) {
            eventDateRangeHelpLabel.setText("End Date cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
            return false;
        }
        else if(!isEndTimeValid) {
            eventDateRangeHelpLabel.setText("End Time cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
            return false;
        }
        else if(!isDateRangeValid) {
            eventDateRangeHelpLabel.setText("Date Range is invalid (start > end)");
            eventDateRangeHelpLabel.setDisable(false);
            return false;
        }
        else {
            return true;
        }
    }
    private void populateEvents() {
        //TODO ???
        ObservableList<Event> events = FXCollections.observableArrayList();
        List<Event> allEvents = eventRepo.findAll();

        if(!allEvents.isEmpty()) {
            events.addAll(allEvents);
        }
        eventsListView.setItems(events);

    }

    //Button Actions - Location Form
    private void addLocation() {
        String name = locationNameField.getText();
        String audienceCapacity = locationAudienceCapacity.getText();
        String extraInfo = locationInformation.getText();

        boolean isNameValid = Objects.nonNull(name);
        boolean isAudienceCapacityValid =
                StringUtils.hasText(audienceCapacity)
                        && Pattern.matches("^[1-9]\\d{0,4}", audienceCapacity);
        if(isAudienceCapacityValid && isNameValid) {
            if ("Add".equals(addLocationButton.getText())) {
                locationRepo.save(new Location(UUID.randomUUID(), name, extraInfo, Integer.parseInt(audienceCapacity)));
            } else {
                Location location = locationListView.getSelectionModel().getSelectedItem();
                location.setLocationName(name);
                location.setCapacity(Integer.parseInt(audienceCapacity));
                location.setLocationInfo(extraInfo);
                locationRepo.save(location);
            }
        }
        populateLocations();
        resetLocationForm();
    }
    private void editLocation() {
        resetLocationForm();
        locationFormLabel.setText("Edit Location");
        Location location = locationListView.getSelectionModel().getSelectedItem();
        locationNameField.setText(location.getLocationName());
        locationAudienceCapacity.setText(String.valueOf(location.getCapacity()));
        locationInformation.setText(location.getLocationInfo());
        saveEventButton.setText("Edit Location");
    }
    private void viewLocation() {
        resetLocationForm();
        locationFormLabel.setText("View Location");
        Location location = locationListView.getSelectionModel().getSelectedItem();
        locationNameField.setText(location.getLocationName());
        locationNameField.setEditable(false);
        locationAudienceCapacity.setText(String.valueOf(location.getCapacity()));
        locationAudienceCapacity.setEditable(false);
        locationInformation.setText(location.getLocationInfo());
        locationInformation.setEditable(false);
        addLocationButton.setVisible(false);
    }
    private void deleteLocation() {
        Location location = locationListView.getSelectionModel().getSelectedItem();
        locationRepo.delete(location);
    }

    //Util Methods - Location Form
    private void populateLocations() {

        ObservableList<Location> locations = FXCollections.observableArrayList();

        if(locationRepo.count() != 0) {
            locations.addAll(locationRepo.findAll());
        }
        locationComboBox.setItems(locations);
        int i = locationListView.getSelectionModel().getSelectedIndex();
        locationListView.setItems(locations);
        locationListView.getSelectionModel().select(i);
    }
    private void resetLocationForm() {
        locationFormLabel.setText("Add Location");
        locationNameField.setEditable(true);
        locationAudienceCapacity.setEditable(true);
        locationInformation.setEditable(true);
        addLocationButton.setVisible(true);
        addLocationButton.setText("Add");
        locationNameField.clear();
        locationAudienceCapacity.clear();
        locationInformation.clear();
    }


    public AnchorPane getScene() {
        return pane;
    }
}