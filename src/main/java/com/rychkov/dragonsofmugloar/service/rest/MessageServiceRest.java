package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Messages;
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
public class MessageServiceRest {
    private final static String GET_MESSAGES_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/messages";
    private RestTemplate restTemplate;
    private HttpEntity httpEntity;

    @Autowired
    public MessageServiceRest(RestTemplate restTemplate, HttpEntity httpEntity) {
        this.restTemplate = restTemplate;
        this.httpEntity = httpEntity;
    }

    @Retryable
    public Messages getMessages(Game game) {
        String url = String.format(GET_MESSAGES_ENDPOINT, game.getGameId());

        log.info("getting messages list for game ={}, turn ={}", game.getGameId(), game.getTurn());
        ResponseEntity<Messages> messagesResponseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Messages.class);

        return messagesResponseEntity.getBody();
    }
}
