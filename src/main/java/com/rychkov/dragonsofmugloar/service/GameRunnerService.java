package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.*;
import com.rychkov.dragonsofmugloar.service.rest.GameServiceRest;
import com.rychkov.dragonsofmugloar.service.rest.MessageServiceRest;
import com.rychkov.dragonsofmugloar.service.rest.ReputationServiceRest;
import com.rychkov.dragonsofmugloar.service.rest.ShopServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GameRunnerService {
    private GameServiceRest gameServiceRest;
    private MessageServiceRest messageServiceRest;
    private ReputationServiceRest reputationServiceRest;
    private ShopServiceRest shopServiceRest;

    @Autowired
    public GameRunnerService(GameServiceRest gameServiceRest,
                             MessageServiceRest messageServiceRest,
                             ReputationServiceRest reputationServiceRest,
                             ShopServiceRest shopServiceRest){
        this.gameServiceRest = gameServiceRest;
        this.messageServiceRest = messageServiceRest;
        this.reputationServiceRest = reputationServiceRest;
        this.shopServiceRest = shopServiceRest;
    }
    
    public void runGames() {
        int bestScore = 0;
        
        do {
            Game game = gameServiceRest.startNewGame();
            log.info("new game started with id ={}", game.getGameId());

            while(game.getLives()>0){
                checkReputation(game);
                visitStore(game);

            }

            log.info("gameover with score ={}", game.getScore());

        } while (bestScore<1000);
    }

    private void visitStore(Game game) {
        log.info("visitiong store for game ={} and turn ={}", game.getGameId(), game.getTurn());

        Items items = shopServiceRest.getShopItems(game);
        List<Item> itemList = items.getItems();

        int lowestPrice = itemList.get(0).getCost();
        Item cheapestItem = new Item();

        for(Item item : itemList){
            if(item.getCost()<lowestPrice){
                lowestPrice = item.getCost();
                cheapestItem = item;
            }
        }

        log.info("cheapest item is ={} for ={} gold", cheapestItem.getName(), cheapestItem.getCost());

        log.info("trying to spend some gold...");

        while(game.getGold() > lowestPrice){
            spendGold(game, items);
        }

        log.info("all gold is spended...");


    }

    private void spendGold(Game game, Items items) {
        List<Item> itemList = items.getItems();
        Item randomItem = itemList.get((int) Math.round(Math.random() * itemList.size()));

        log.info("trying to buy item ={} for ={} gold", randomItem.getName(), randomItem.getCost());

        ShoppingResult shoppingResult = shopServiceRest.purchaseAnItem(game, randomItem);

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


    private void checkReputation(Game game){
        log.info("checking reputation...");

        Reputation reputation = reputationServiceRest.getReputation(game);

        log.info("reputation: ={}", reputation);
    }
}
