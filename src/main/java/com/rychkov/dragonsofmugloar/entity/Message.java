package com.rychkov.dragonsofmugloar.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String adId;
    private String message;
    private String reward;
    private int expiresIn;
}
