package com.cougarneticit.gims.controller.admin;

import com.cougarneticit.gims.controller.common.GIMSController;
import com.cougarneticit.gims.model.Customer;
import com.cougarneticit.gims.model.Event;
import com.cougarneticit.gims.model.Room;
import com.cougarneticit.gims.model.Stay;
import com.cougarneticit.gims.model.repos.CustomerRepo;
import com.cougarneticit.gims.model.repos.EventRepo;
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
import org.springframework.util.CollectionUtils;

import java.net.URL;

import java.util.*;

@Component
@FxmlView("/CustomersSceneController.fxml")
public class CustomersSceneController extends GIMSController implements Initializable {

    private Stage stage;



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

            populateCustomerListView();
            resetCustomerForm();
        }

    }
    private void viewCustomer() {
        customerFormLabel.setText("View a Customer");
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();


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

    public AnchorPane getScene() {
        return pane;
    }
}
