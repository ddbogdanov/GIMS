package com.cougarneticit.gims;

import com.cougarneticit.gims.application.SpringbootJavaFxApplication;
import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GimsApplication {

	@Autowired
	ApplicationContext springContext;

	public static void main(String[] args) {
		Application.launch(SpringbootJavaFxApplication.class, args);
	}

	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext springContext) {
		return new SpringFxWeaver(springContext);
	}

}
