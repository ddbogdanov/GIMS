package com.cougarneticit.gims.controller.common;

import com.cougarneticit.gims.model.User;
import com.jfoenix.controls.JFXButton;
import net.rgielen.fxweaver.core.FxWeaver;

import java.util.ArrayList;

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
}
