package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.service.rest.GameServiceRest;
import com.rychkov.dragonsofmugloar.service.rest.MessageServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameRunnerService {
    private GameServiceRest gameServiceRest;
    private MessageServiceRest messageServiceRest;

    @Autowired
    public GameRunnerService(GameServiceRest gameServiceRest,
                             MessageServiceRest messageServiceRest){
        this.gameServiceRest = gameServiceRest;
        this.messageServiceRest = messageServiceRest;
    }
    
    public void runGames() {
        int bestScore = 0;
        
        do {
            Game newGame = gameServiceRest.startNewGame();
            // TODO: 26.04.2020 LOGS 
        } while (bestScore<1000);
    }
}
