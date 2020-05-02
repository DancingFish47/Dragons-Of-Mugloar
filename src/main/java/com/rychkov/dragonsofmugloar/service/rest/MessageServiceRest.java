package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Message;
import com.rychkov.dragonsofmugloar.entity.MessageResult;
import com.rychkov.dragonsofmugloar.entity.Messages;
import com.rychkov.dragonsofmugloar.exception.CorruptedMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MessageServiceRest {
    private final static String GET_MESSAGES_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/messages";
    private final static String SOLVE_MESSAGE_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/solve/%s";
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

        ResponseEntity<Message[]> messagesResponseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Message[].class);

        List<Message> messageList = Arrays.asList(messagesResponseEntity.getBody());

        return new Messages(messageList);
    }

    @Retryable
    public MessageResult solveMessage(Game game, Message message) throws CorruptedMessageException{
        String url = String.format(SOLVE_MESSAGE_ENDPOINT, game.getGameId(), message.getAdId());

        log.info("solving message ={} for game ={}", message.getAdId(), game.getGameId());
        ResponseEntity<MessageResult> messageResultResponseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, MessageResult.class);

        return messageResultResponseEntity.getBody();
    }

}
