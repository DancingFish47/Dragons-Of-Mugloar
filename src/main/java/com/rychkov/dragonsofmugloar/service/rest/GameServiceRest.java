package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.entity.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GameServiceRest{
    private final static String START_GAME_ENDPOINT = "https://dragonsofmugloar.com/api/v2/game/start";

    private RestTemplate restTemplate;
    private HttpEntity httpEntity;

    @Autowired
    public GameServiceRest(RestTemplate restTemplate, HttpEntity httpEntity){
        this.restTemplate = restTemplate;
        this.httpEntity = httpEntity;
    }

    @Retryable
    public Game startNewGame(){
        log.info("starting new game...");
        ResponseEntity<Game> game = restTemplate.exchange(START_GAME_ENDPOINT, HttpMethod.POST, httpEntity, Game.class);
        return game.getBody();
    }
}
