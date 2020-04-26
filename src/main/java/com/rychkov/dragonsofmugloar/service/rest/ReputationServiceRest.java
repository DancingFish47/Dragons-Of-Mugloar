package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Reputation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ReputationServiceRest {
    private final static String GET_REPUTATION_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/investigate/reputation";
    private RestTemplate restTemplate;
    private HttpEntity httpEntity;

    @Autowired
    public ReputationServiceRest(RestTemplate restTemplate, HttpEntity httpEntity){
        this.restTemplate = restTemplate;
        this.httpEntity = httpEntity;
    }

    @Retryable
    public Reputation getReputation(Game game){
        String url = String.format(GET_REPUTATION_ENDPOINT, game.getGameId());

        log.info("getting reputation for game ={} turn ={}", game.getGameId(), game.getTurn());
        ResponseEntity<Reputation> reputationResponseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Reputation.class);

        return reputationResponseEntity.getBody();
    }
}
