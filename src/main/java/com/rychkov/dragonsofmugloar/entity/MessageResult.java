package com.rychkov.dragonsofmugloar.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResult {
    private boolean success;
    private int lives;
    private int gold;
    private int score;
    private int highscore;
    private int turn;
    private String message;
}
