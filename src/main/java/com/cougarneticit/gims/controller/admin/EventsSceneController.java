package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Customer;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

@Component
@FxmlView("/EventsSceneController.fxml")
public class EventsSceneController extends GIMSController implements Initializable {

    private Stage stage;

    @FXML private AnchorPane pane;
    @FXML private JFXListView<Event> eventsListView;
    @FXML private JFXListView<Customer> eventCustomerListView;
    @FXML private JFXListView<Location> locationListView;
    @FXML private JFXButton editEventButton, viewEventButton, deleteEventButton, editLocationButton, viewLocationButton, deleteLocationButton;
    @FXML private Label eventFormLabel, locationFormLabel, eventNameHelpLabel, eventDateRangeHelpLabel;
    @FXML private JFXComboBox<Location> locationComboBox;
    @FXML private JFXTextField eventNameTextField, locationNameField, locationAudienceCapacity;
    @FXML private JFXDatePicker eventStartDate, eventEndDate;
    @FXML private JFXTimePicker eventStartTime, eventEndTime;
    @FXML private JFXButton saveEventButton, cancelEventButton, addLocationButton, cancelLocationButton;
    @FXML private JFXTextArea eventExtraInfo, locationInformation;

    @Autowired EventRepo eventRepo;
    @Autowired LocationRepo locationRepo;

    public EventsSceneController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    private BiFunction<LocalDate, LocalTime, Date> dateMaker = (date, time) -> Date.from(date.atTime(time).toInstant(ZoneOffset.UTC));

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.stage = new Stage();
        initStage(stage, pane, null, null, null, null, true);
        populateEvents();
        populateLocations();

        //Events form
        saveEventButton.setOnAction(e -> addEvents());
        editEventButton.setOnAction(e -> editEvent());
        viewEventButton.setOnAction(e -> viewEvent());
        deleteEventButton.setOnAction(e -> deleteEvent());
        cancelEventButton.setOnAction(e -> resetEventForm());

        //Location form
        addLocationButton.setOnAction(e -> addLocation());
        editLocationButton.setOnAction(e -> editLocation());
        viewLocationButton.setOnAction(e -> viewLocation());
        deleteLocationButton.setOnAction(e -> deleteLocation());
        cancelLocationButton.setOnAction(e -> resetLocationForm());

    }

    //Button Actions
    private void addEvents() {

        Location location = locationComboBox.getSelectionModel().getSelectedItem();
        String name = eventNameTextField.getText();
        LocalDate startDate = eventStartDate.getValue();
        LocalTime startTime = eventStartTime.getValue();
        LocalDate endDate = eventEndDate.getValue();
        LocalTime endTime = eventEndTime.getValue();
        String extraInfo = eventExtraInfo.getText();

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

        if(isNameValid && isLocationValid && isDateRangeValid) {
            if ("Add Event".equals(saveEventButton.getText())) {
                eventRepo.save(new Event(name, extraInfo, dateMaker.apply(startDate, startTime), dateMaker.apply(endDate, endTime), location));
            }
            else {
                Event event = eventsListView.getSelectionModel().getSelectedItem();
                event.setEventName(name);
                event.setEventInfo(extraInfo);
                event.setStartDate(dateMaker.apply(startDate, startTime));
                event.setEndDate(dateMaker.apply(endDate, endTime));
                event.setLocation(location);
                eventRepo.save(event);
            }
        }
        else if(!isNameValid) {
            eventNameHelpLabel.setText("Event Name should contain characters");
            eventNameHelpLabel.setDisable(false);
        }
        else if(!isStartDateValid) {
            eventDateRangeHelpLabel.setText("Start Date cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
        }
        else if(!isStartTimeValid) {
            eventDateRangeHelpLabel.setText("Start Time cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
        }
        else if(!isEndDateValid) {
            eventDateRangeHelpLabel.setText("End Date cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
        }
        else if(!isEndTimeValid) {
            eventDateRangeHelpLabel.setText("End Time cannot be null");
            eventDateRangeHelpLabel.setDisable(false);
        }
        else if(!isDateRangeValid) {
            eventDateRangeHelpLabel.setText("Date Range is invalid (start > end)");
            eventDateRangeHelpLabel.setDisable(false);
        }
        populateEvents();
    }
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
                locationRepo.save(new Location(name, extraInfo, Integer.parseInt(audienceCapacity)));
            }
            else {
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
    private void editEvent() {
        resetEventForm();
        eventFormLabel.setText("Edit Event");

        Event event = eventsListView.getSelectionModel().getSelectedItem();

        locationComboBox.getSelectionModel().select(event.getLocation());
        eventNameTextField.setText(event.getEventName());
        OffsetDateTime offsetDateTime = event.getStartDate().toInstant().atOffset(ZoneOffset.UTC);
        eventStartDate.setValue(offsetDateTime.toLocalDate());
        eventStartTime.setValue(offsetDateTime.toLocalTime());
        offsetDateTime = event.getEndDate().toInstant().atOffset(ZoneOffset.UTC);
        eventEndDate.setValue(offsetDateTime.toLocalDate());
        eventEndTime.setValue(offsetDateTime.toLocalTime());
        eventExtraInfo.setText(event.getEventInfo());
        saveEventButton.setText("Edit Event");
    }
    private void viewEvent() {
        //TODO: Invert relationship between event and customers
        /*resetEventForm();
        eventFormLabel.setText("View Event");
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        locationComboBox.getSelectionModel().select(event.getLocation());
        eventCustomerListView.setItems(
                FXCollections
                        .observableArrayList(Optional.ofNullable(event.getCustomers()).orElse(Collections.EMPTY_SET)));
        locationComboBox.setDisable(true);
        eventNameTextField.setText(event.getEventName());
        eventNameTextField.setEditable(false);
        OffsetDateTime offsetDateTime = event.getStartDate().toInstant().atOffset(ZoneOffset.UTC);
        eventStartDate.setValue(offsetDateTime.toLocalDate());
        eventStartDate.setDisable(true);
        eventStartTime.setValue(offsetDateTime.toLocalTime());
        eventStartTime.setDisable(true);
        offsetDateTime = event.getEndDate().toInstant().atOffset(ZoneOffset.UTC);
        eventEndDate.setValue(offsetDateTime.toLocalDate());
        eventEndDate.setDisable(true);
        eventEndTime.setValue(offsetDateTime.toLocalTime());
        eventEndTime.setDisable(true);
        eventExtraInfo.setText(event.getEventInfo());
        eventExtraInfo.setEditable(false);
        saveEventButton.setVisible(false);*/
    }
    private void deleteEvent() {
        Event event = eventsListView.getSelectionModel().getSelectedItem();
        eventRepo.delete(event);
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
        eventStartDate.setDisable(false);
        eventStartTime.setDisable(false);
        eventEndDate.setDisable(false);
        eventEndTime.setDisable(false);
        eventExtraInfo.setEditable(true);
        locationComboBox.getSelectionModel().clearSelection();
        eventNameTextField.clear();
        eventStartDate.setValue(null);
        eventStartTime.setValue(null);
        eventEndDate.setValue(null);
        eventEndTime.setValue(null);
        eventCustomerListView.getSelectionModel().clearSelection();
        eventExtraInfo.clear();
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
    //Util Methods
    private void populateEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();

        if(eventRepo.count() != 0) {
            events.addAll(eventRepo.findAll());
        }
        eventsListView.setItems(events);
    }
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
    private void deleteLocation() {
        Location location = locationListView.getSelectionModel().getSelectedItem();
        locationRepo.delete(location);
    }

    public AnchorPane getScene() {
        return pane;
    }
}