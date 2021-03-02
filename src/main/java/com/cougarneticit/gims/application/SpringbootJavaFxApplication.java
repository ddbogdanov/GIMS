package com.cougarneticit.gims.application;

import com.cougarneticit.gims.GimsApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringbootJavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init()  throws Exception {
        this.springContext = new SpringApplicationBuilder()
                .sources(GimsApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        springContext.publishEvent(new StageReadyEvent(primaryStage));
    }
    @Override
    public void stop() throws Exception {
        this.springContext.close();
        Platform.exit();
    }
}
