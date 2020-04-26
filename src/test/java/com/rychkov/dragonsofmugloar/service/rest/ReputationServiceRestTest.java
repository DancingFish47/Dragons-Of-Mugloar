package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.TestRoot;
import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Reputation;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReputationServiceRestTest extends TestRoot {
    private final static String GAME_ID = "gameId";

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private ReputationServiceRest reputationServiceRest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReputationReturnsValidReputation() {
        Game game = new Game();
        game.setGameId(GAME_ID);

        Reputation reputation = new Reputation();
        ResponseEntity<Reputation> expectedResponseEntity = new ResponseEntity<>(reputation, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Reputation.class)))
                .thenReturn(expectedResponseEntity);

        Reputation actualReputation = reputationServiceRest.getReputation(game);

        assertThat(actualReputation, is(equalTo(reputation)));

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Reputation.class));
    }
}
