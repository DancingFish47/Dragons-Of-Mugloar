package com.rychkov.dragonsofmugloar.service.rest;

import com.rychkov.dragonsofmugloar.TestRoot;
import com.rychkov.dragonsofmugloar.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShopServiceRestTest extends TestRoot {
    private final static String GAME_ID = "gameId";
    private final static String ITEM_ID = "itemId";

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private ShopServiceRest shopServiceRest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getShopItemsReturnsValidItems() {
        Game game = new Game();
        game.setGameId(GAME_ID);

        Items items = new Items();

        ResponseEntity<Items> expectedResponseEntity = new ResponseEntity<>(items, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Items.class)))
                .thenReturn(expectedResponseEntity);

        Items actualItems = shopServiceRest.getShopItems(game);

        assertThat(actualItems, is(equalTo(items)));

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Items.class));
    }

    @Test
    public void purchaseAnItemReturnsValidShoppingResult(){
        Game game = new Game();
        game.setGameId(GAME_ID);

        Item item = new Item();
        item.setId(ITEM_ID);

        ShoppingResult shoppingResult = new ShoppingResult();

        ResponseEntity<ShoppingResult> expectedResponseEntity = new ResponseEntity<>(shoppingResult, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ShoppingResult.class)))
                .thenReturn(expectedResponseEntity);

        ShoppingResult actualShoppingResult = shopServiceRest.purchaseAnItem(game, item);

        assertThat(actualShoppingResult, is(equalTo(shoppingResult)));

        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ShoppingResult.class));
    }
}
