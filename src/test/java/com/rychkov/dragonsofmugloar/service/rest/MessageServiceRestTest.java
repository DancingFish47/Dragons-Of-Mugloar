package com.rychkov.dragonsofmugloar.service.rest;


import com.rychkov.dragonsofmugloar.TestRoot;
import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Messages;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageServiceRestTest extends TestRoot {
    private final static String GAME_ID = "gameId";

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MessageServiceRest messageServiceRest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getMessagesReturnsValidMessages() {
        Game game = new Game();
        game.setGameId(GAME_ID);

        Messages messages = new Messages();
        ResponseEntity<Messages> expectedResponseEntity = new ResponseEntity<>(messages, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Messages.class)))
                .thenReturn(expectedResponseEntity);

        Messages actualMessages = messageServiceRest.getMessages(game);

        assertThat(actualMessages, is(equalTo(messages)));

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Messages.class));
    }
}
