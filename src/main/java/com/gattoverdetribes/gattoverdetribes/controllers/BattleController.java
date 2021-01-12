package com.gattoverdetribes.gattoverdetribes.controllers;

import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.services.BattleService;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BattleController {
  private final BattleService battleService;
  private final PlayerService playerService;

  @Autowired
  public BattleController(
      BattleService battleService,
      PlayerService playerService) {
    this.battleService = battleService;
    this.playerService = playerService;
  }

/*  @PostMapping("/kingdom/fight/{id}")
  public ResponseEntity<?> upgradeBuilding(@RequestHeader(name = "X-tribes-token") String token,
      @PathVariable Long id) {
    Player player = playerService.extractPlayerFromToken(token);
    battleService.battleKingdoms(player.getKingdom(),id);
  }*/

}
