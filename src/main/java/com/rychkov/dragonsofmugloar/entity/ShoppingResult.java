package com.rychkov.dragonsofmugloar.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingResult {
    private boolean shoppingSuccess;
    private int gold;
    private int lives;
    private int level;
    private int turn;
}
