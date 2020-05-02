package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Message;
import com.rychkov.dragonsofmugloar.entity.MessageResult;
import com.rychkov.dragonsofmugloar.entity.Messages;
import com.rychkov.dragonsofmugloar.exception.CorruptedMessageException;
import com.rychkov.dragonsofmugloar.service.rest.MessageServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MessageService {
    private MessageServiceRest messageServiceRest;
    private DiaryService diaryService;

    @Autowired
    public MessageService(MessageServiceRest messageServiceRest, DiaryService diaryService){
        this.messageServiceRest = messageServiceRest;
        this.diaryService = diaryService;
    }

    public void solveSomething(Game game, Map<String, Integer> diary) {
        Messages availableMessages = messageServiceRest.getMessages(game);

        List<Message> messages = availableMessages.getMessages();

        Message message = pickSomethingEasy(messages, diary);

        try {
            MessageResult messageResult = solveMessage(game, message);
            diaryService.makeANoteAboutResult(messageResult, message, diary);
        } catch (CorruptedMessageException e) {
            log.info("message is corrupted, trying to solve something else...");
            solveSomething(game, diary);
        }
    }

    private MessageResult solveMessage(Game game, Message message) throws CorruptedMessageException {
        log.info("trying to solve message ={}", message.getAdId());

        MessageResult messageResult = messageServiceRest.solveMessage(game, message);

        log.info("message solving result ={}", messageResult.isSuccess());
        log.info("message result: ={}", messageResult.getMessage());

        log.info("updating game...");

        updateGame(messageResult, game);

        return messageResult;
    }

    private Message pickSomethingEasy(List<Message> messages, Map<String, Integer> diary) {
        Map<String, Integer> sortedMessagesByDifficulty = sortByValue(diary);
        List<Message> easyMessages = new ArrayList<>();
        sortedMessagesByDifficulty.forEach((s, integer) -> {
            if (integer > 0) {
                Optional<Message> message = messages.stream()
                        .filter(m -> m.getProbability().equals(s))
                        .findFirst();

                message.ifPresent(easyMessages::add);
            }
        });
        if (!easyMessages.isEmpty()) return easyMessages.get(0);

        return pickRandomMessage(messages);
    }

    private Message pickRandomMessage(List<Message> messages) {
        return messages.get((int) Math.floor(Math.random() * messages.size()));
    }

    private static <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private void updateGame(MessageResult messageResult, Game game) {
        log.info("updating game after message solving...");

        game.setGold(messageResult.getGold());
        game.setLives(messageResult.getLives());
        game.setTurn(messageResult.getTurn());
        game.setScore(messageResult.getScore());
        game.setHighScore(messageResult.getHighscore());

        log.info("game is updated");
    }
}
