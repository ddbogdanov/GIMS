package com.cougarneticit.gims.controller.common;

import com.cougarneticit.gims.model.User;
import net.rgielen.fxweaver.core.FxWeaver;

public class GIMSController {

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
}
