package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.ErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BattleServiceImpl implements BattleService {

  private final KingdomService kingdomService;

  @Autowired
  public BattleServiceImpl(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

/*  @Override
  public ResponseEntity<?> battleKingdoms(Kingdom playerKingdom, Long id) {
    if(playerKingdom.getId().equals(id)){
      return ResponseEntity.status(400).body(new ErrorResponseDTO("You cannot battle yourself"));
    }
    Kingdom opponentKingdom = kingdomService.getById(id);
    List<Troop> playerTroops = playerKingdom.getTroops();
    List<Troop> opponentTroops = opponentKingdom.getTroops();
  }*/
}
