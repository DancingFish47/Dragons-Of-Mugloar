package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.TestRoot;
import com.rychkov.dragonsofmugloar.entity.Game;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameServiceRestTest extends TestRoot {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private GameServiceRest gameServiceRest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startNewGameReturnsANewGame() {
        Game game = new Game();

        ResponseEntity<Game> expectedResponseEntity = new ResponseEntity<>(game, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Game.class)))
                .thenReturn(expectedResponseEntity);

        Game actualGame = gameServiceRest.startNewGame();

        assertThat(actualGame, is(equalTo(game)));

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Game.class));
    }

}
