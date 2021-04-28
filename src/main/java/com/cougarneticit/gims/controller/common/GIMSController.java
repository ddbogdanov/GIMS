package com.cougarneticit.gims.controller.common;

import com.cougarneticit.gims.application.ResizeHelper;
import com.cougarneticit.gims.model.User;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GIMSController {

    private static final String ACTIVE_BUTTON ="-fx-background-color: #11AB97";
    private static final String INACTIVE_BUTTON ="-fx-background-color: #007B68";

    private static User activeUser;
    protected final FxWeaver fxWeaver;

    public GIMSController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public void setActiveUser(User activeUser) {
        GIMSController.activeUser = activeUser;
    }
    public User getActiveUser() {
        return activeUser;
    }

    public void initStage(Stage stage, Pane parentPane, String title, StageStyle stageStyle, Modality modality, Color sceneFill, boolean isResizable) {
        stage.setTitle(title);
        stage.initStyle(stageStyle);
        stage.initModality(modality);
        stage.setResizable(isResizable);
        Scene scene = new Scene(parentPane);
        scene.setFill(sceneFill);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
    }

    //there was a better way to do this. :)
    public void setActiveButton(JFXButton button, ArrayList<JFXButton> buttonList) {
        for(JFXButton b : buttonList) {
            if(button.getId().equals(b.getId())) {
                b.setStyle(ACTIVE_BUTTON);
            }
            else {
                b.setStyle(INACTIVE_BUTTON);
            }
        }
    }

    protected boolean validateEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }
    protected boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    protected boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("^(\\d{3}[-]?){2}\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    
    protected boolean validateState(String state) {
        return true;
    }
    
    protected boolean validateCountry(String phone) {
        return true;
    }
}
