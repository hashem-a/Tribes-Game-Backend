package com.gattoverdetribes.gattoverdetribes.controllers;

import com.gattoverdetribes.gattoverdetribes.dtos.TroopDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import com.gattoverdetribes.gattoverdetribes.services.TroopService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom")
public class TroopController {

  private final PlayerService playerService;
  private final TroopService troopService;

  @Autowired
  public TroopController(PlayerService playerService,
      TroopService troopService) {
    this.playerService = playerService;
    this.troopService = troopService;
  }

  @GetMapping("/troops")
  public ResponseEntity<List<TroopDetailsDTO>> getTroopsByKingdom(
      @RequestHeader(name = "X-tribes-token") String token) {
    Kingdom kingdom = playerService.extractPlayerFromToken(token).getKingdom();
    List<TroopDetailsDTO> listTroops = troopService.getTroopsByKingdom(kingdom);
    if (listTroops.isEmpty()) {
      return new ResponseEntity<>(listTroops, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(listTroops, HttpStatus.OK);
  }

  @PostMapping("/troops/buy")
  public ResponseEntity<String> createTroop(@RequestHeader(name = "X-tribes-token") String token) {
    Player player = playerService.extractPlayerFromToken(token);

    troopService.createTroopForKingdom("archer", player.getKingdom());
    troopService.createTroopForKingdom("horseman", player.getKingdom());
    troopService.createTroopForKingdom("swordsman", player.getKingdom());
    return ResponseEntity.ok("Troops created successfully.");
  }
}