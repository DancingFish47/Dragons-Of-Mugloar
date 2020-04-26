package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class ShopServiceRest {
    private final static String GET_SHOP_ITEMS_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/shop";
    private final static String PURCHASE_AN_ITEM_ENDPOINT = "https://dragonsofmugloar.com/api/v2/%s/shop/buy/%s";
    private RestTemplate restTemplate;
    private HttpEntity httpEntity;

    @Autowired
    public ShopServiceRest(RestTemplate restTemplate, HttpEntity httpEntity){
        this.restTemplate = restTemplate;
        this.httpEntity = httpEntity;
    }

    @Retryable
    public Items getShopItems(Game game){
        String url = String.format(GET_SHOP_ITEMS_ENDPOINT, game.getGameId());

        log.info("getting items from the shop for game ={} turn ={}", game.getGameId(), game.getTurn());
        ResponseEntity<Items> itemsResponseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Items.class);

        return itemsResponseEntity.getBody();
    }

    @Retryable
    public ShoppingResult purchaseAnItem(Game game, Item item){
        String url = String.format(PURCHASE_AN_ITEM_ENDPOINT, game.getGameId(), item.getId());

        log.info("trying to purchase item ={} for ={} gold in game ={} turn ={}", item.getName(), item.getCost(), game.getGameId(), game.getTurn());
        ResponseEntity<ShoppingResult> shoppingResultResponseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ShoppingResult.class);

        return shoppingResultResponseEntity.getBody();
    }
}
