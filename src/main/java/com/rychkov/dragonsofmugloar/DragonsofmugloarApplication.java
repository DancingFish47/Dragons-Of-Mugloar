package com.rychkov.dragonsofmugloar;

import com.rychkov.dragonsofmugloar.service.GameRunnerService;
import com.rychkov.dragonsofmugloar.service.rest.GameServiceRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DragonsofmugloarApplication {
    private GameRunnerService gameRunnerService;

    @Autowired
    public DragonsofmugloarApplication(GameRunnerService gameRunnerService){
        this.gameRunnerService = gameRunnerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DragonsofmugloarApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startGames() {
        gameRunnerService.runGames();
    }

}
