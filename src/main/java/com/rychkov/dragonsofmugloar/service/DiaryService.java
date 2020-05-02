package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Message;
import com.rychkov.dragonsofmugloar.entity.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DiaryService {
    public void makeANoteAboutResult(MessageResult messageResult, Message message, Map<String, Integer> diary) {
        if (diary.containsKey(message.getProbability())) {
            if (messageResult.isSuccess()) {
                diary.put(message.getProbability(), diary.get(message.getProbability()) + 1);
            } else {
                diary.put(message.getProbability(), diary.get(message.getProbability()) - 1);
            }
        } else {
            if (messageResult.isSuccess()) {
                diary.put(message.getProbability(), 1);
            } else {
                diary.put(message.getProbability(), -1);
            }
        }
    }
}
