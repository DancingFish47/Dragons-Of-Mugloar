package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Item;
import com.rychkov.dragonsofmugloar.entity.Items;
import com.rychkov.dragonsofmugloar.entity.ShoppingResult;
import com.rychkov.dragonsofmugloar.service.rest.ShopServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopService {
    private ShopServiceRest shopServiceRest;

    @Autowired
    public ShopService(ShopServiceRest shopServiceRest) {
        this.shopServiceRest = shopServiceRest;
    }

    public void visitStore(Game game) {
        log.info("visitiong store for game ={} and turn ={}", game.getGameId(), game.getTurn());

        Items items = shopServiceRest.getShopItems(game);
        List<Item> itemList = items.getItems();

        int lowestPrice = itemList.get(0).getCost();
        Item cheapestItem = new Item();

        for (Item item : itemList) {
            if (item.getCost() < lowestPrice) {
                lowestPrice = item.getCost();
                cheapestItem = item;
            }
        }

        log.info("cheapest item is ={} for ={} gold", cheapestItem.getName(), cheapestItem.getCost());

        log.info("trying to spend some gold...");

        //while (game.getGold() > lowestPrice) {
        while (game.getGold() > 50) {
            buyHealingPotions(game, items);
        }

        log.info("all gold is spended...");
    }

    private void spendGold(Game game, Items items) {
        List<Item> itemList = items.getItems();
        Item randomItem = itemList.get((int) Math.floor(Math.random() * itemList.size()));

        log.info("trying to buy item ={} for ={} gold", randomItem.getName(), randomItem.getCost());

        ShoppingResult shoppingResult = shopServiceRest.purchaseAnItem(game, randomItem);

        log.info("buying result ={}", shoppingResult.isShoppingSuccess());

        updateGame(shoppingResult, game);
    }

    private void buyHealingPotions(Game game, Items items) {
        List<Item> itemList = items.getItems();
        Item healingPotion = itemList.stream()
                .filter(item -> item.getName().equals("Healing potion"))
                .findFirst()
                .get();

        log.info("trying to buy item ={} for ={} gold", healingPotion.getName(), healingPotion.getCost());

        ShoppingResult shoppingResult = shopServiceRest.purchaseAnItem(game, healingPotion);

        log.info("buying result ={}", shoppingResult.isShoppingSuccess());

        updateGame(shoppingResult, game);
    }

    private void updateGame(ShoppingResult shoppingResult, Game game) {
        log.info("updating game after purchase try...");

        game.setGold(shoppingResult.getGold());
        game.setLevel(shoppingResult.getLevel());
        game.setLives(shoppingResult.getLives());
        game.setTurn(shoppingResult.getTurn());

        log.info("game is updated");
    }
}
