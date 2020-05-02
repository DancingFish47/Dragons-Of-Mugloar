package com.rychkov.dragonsofmugloar.service;

import com.rychkov.dragonsofmugloar.entity.Game;
import com.rychkov.dragonsofmugloar.entity.Reputation;
import com.rychkov.dragonsofmugloar.service.rest.ReputationServiceRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReputationService {
    private ReputationServiceRest reputationServiceRest;

    @Autowired
    public ReputationService(ReputationServiceRest reputationServiceRest) {
        this.reputationServiceRest = reputationServiceRest;
    }

    public void checkReputation(Game game) {
        log.info("checking reputation...");

        Reputation reputation = reputationServiceRest.getReputation(game);

        log.info("reputation: ={}", reputation);
    }
}
