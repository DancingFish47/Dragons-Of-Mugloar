package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.service.rest.GameServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GameRunnerService {
    private GameServiceRest gameServiceRest;
    private MessageService messageService;
    private ReputationService reputationService;
    private ShopService shopService;

    private volatile List<Integer> results = new ArrayList<>();
    private volatile Map<String, Integer> diary = new HashMap<>();

    @Autowired
    public GameRunnerService(GameServiceRest gameServiceRest,
                             MessageService messageService,
                             ReputationService reputationService,
                             ShopService shopService) {
        this.gameServiceRest = gameServiceRest;
        this.messageService = messageService;
        this.reputationService = reputationService;
        this.shopService = shopService;
    }

    public void runGames() {
        for (int i = 0; i < 10; i++) {
            Game game = gameServiceRest.startNewGame();
            log.warn("new game started with id ={}", game.getGameId());

            while (game.getLives() > 0) {
                reputationService.checkReputation(game);
                shopService.visitStore(game);
                messageService.solveSomething(game, diary);
                log.info("current score after round = {}...", game.getScore());
            }
            results.add(game.getScore());
            log.warn("gameover with score ={}", game.getScore());

        }

        results.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);

        System.out.println(diary.toString());
    }
}
