package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.BattleWinnerDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.ErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BattleServiceImpl implements BattleService {

  private final KingdomService kingdomService;
  private final TroopService troopService;
  private final ResourceService resourceService;
  private final PlayerService playerService;

  @Autowired
  public BattleServiceImpl(
      KingdomService kingdomService,
      TroopService troopService,
      ResourceService resourceService,
      PlayerService playerService) {
    this.kingdomService = kingdomService;
    this.troopService = troopService;
    this.resourceService = resourceService;
    this.playerService = playerService;
  }

  @Override
  public ResponseEntity<?> battleKingdoms(Kingdom playerKingdom, Long id) {
    if (playerKingdom.getId().equals(id)) {
      return ResponseEntity.status(400).body(new ErrorResponseDTO("You cannot battle yourself"));
    }
    Kingdom opponentKingdom = kingdomService.getById(id);
    List<Troop> playerTroops = playerKingdom.getTroops();
    List<Troop> opponentTroops = opponentKingdom.getTroops();
    int biggestTroopSize = Math.max(playerTroops.size(), opponentTroops.size());

    int i = 0;
    while (i < biggestTroopSize) {
      List<Troop> pairedTroops = pairedTroops(playerTroops, opponentTroops);
      battle(pairedTroops, playerKingdom.getId(), opponentKingdom.getId());
      i++;
    }

    return ResponseEntity.status(200).body(new ErrorResponseDTO("You won bitch"));
  }

  private Troop getRandomTroop(List<Troop> troops) {
    Troop troop = troops.get(new Random().nextInt(troops.size()));
    troops.remove(troop);
    return troop;
  }

  private Building getRandomBuilding(List<Building> buildings) {
    List<Building> buildingsWithoutTownhall =
        buildings.stream()
            .filter(building -> building.getType() != BuildingType.TOWNHALL)
            .collect(Collectors.toList());
    return buildingsWithoutTownhall.get(new Random().nextInt(buildingsWithoutTownhall.size()));
  }

  private Building getTownHallBuilding(List<Building> buildings) {
    return buildings.stream()
        .filter(building1 -> building1.getType() == BuildingType.TOWNHALL)
        .findFirst()
        .get();
  }

  private List<Troop> pairedTroops(List<Troop> playerTroops, List<Troop> opponentTroops) {
    List<Troop> list = new ArrayList<>();
    if (playerTroops.size() == 0 && opponentTroops.size() != 0) {
      Troop randomOpponentTroop = getRandomTroop(opponentTroops);
      list.add(randomOpponentTroop);
      return list;
    } else if (playerTroops.size() != 0 && opponentTroops.size() == 0) {
      Troop randomPlayerTroop = getRandomTroop(playerTroops);
      list.add(randomPlayerTroop);
      return list;
    }
    Troop randomOpponentTroop = getRandomTroop(opponentTroops);
    Troop randomPlayerTroop = getRandomTroop(playerTroops);
    list.add(randomPlayerTroop);
    list.add(randomOpponentTroop);
    return list;
  }
  // logic for results
  // history of battle class 4 list 2 resources
  private ResponseEntity<?> battle(
      List<Troop> pairedTroops, Long playerKingdomId, Long opponentKingdomId) {
    boolean kingdomHasExtraTroops = pairedTroops.size() == 1;
    if (kingdomHasExtraTroops) {
      Troop troop = pairedTroops.get(0);
      Long id = troop.getKingdom().getId();
      Kingdom opponentKingdom;
      if (id.equals(playerKingdomId)) {
        opponentKingdom = kingdomService.getById(opponentKingdomId);
      } else {
        opponentKingdom = kingdomService.getById(playerKingdomId);
      }
      List<Building> opponentBuildings = opponentKingdom.getBuildings();

      if (opponentBuildings.size() != 0) {
        Building randomBuilding;
        if (opponentBuildings.size() == 1) {
          randomBuilding = getTownHallBuilding(opponentBuildings);
        } else {
          randomBuilding = getRandomBuilding(opponentBuildings);
        }

        while (troop.getHp() > 0 && randomBuilding.getHp() > 0) {
          randomBuilding.setHp(randomBuilding.getHp() - troop.getAttack());
          troop.setHp(troop.getHp() - 10);
        }
        if (troop.getHp() <= 0 && randomBuilding.getHp() <= 0) {
          pairedTroops.remove(troop);
          opponentBuildings.remove(randomBuilding);
        } else if (troop.getHp() <= 0) {
          // add to historylist
          // remove from DB
          pairedTroops.remove(troop);
        } else if (randomBuilding.getHp() <= 0) {
          opponentBuildings.remove(randomBuilding);
        }
      }

    } else {
      Troop attackingTroop = getRandomTroop(pairedTroops);
      Troop defendingTroop = pairedTroops.get(0);
      if (attackingTroop.getKingdom().getTroops().size() == 0) {
        return getWinner(attackingTroop, defendingTroop);
      } else if (defendingTroop.getKingdom().getTroops().size() == 0) {
        return getWinner(defendingTroop, attackingTroop);
      }
      battle(attackingTroop, defendingTroop);
    }
    return ResponseEntity.status(400).body(new ErrorResponseDTO("You cannot battle yourself"));
  }

  private ResponseEntity<?> getWinner(Troop attackingTroop, Troop defendingTroop) {
    Player player = defendingTroop.getKingdom().getPlayer();
    Player defeatedPlayer = attackingTroop.getKingdom().getPlayer();
    BattleWinnerDTO battleWinnerDTO = new BattleWinnerDTO();
    battleWinnerDTO.setName(player.getUsername());
    battleWinnerDTO.setResourcesBeforeBattle(player.getKingdom().getResources());
    battleWinnerDTO.setResourcesGainedAfterBattle(defeatedPlayer.getKingdom().getResources());
    player.getKingdom().setResources(defeatedPlayer.getKingdom().getResources());
    playerService.savePlayer(player);
    return ResponseEntity.status(200).body(battleWinnerDTO);
  }

  public void battle(Troop attackingTroop, Troop defendingTroop) {
    while (attackingTroop.getHp() > 0 && defendingTroop.getHp() > 0) {

      int damage = attackingTroop.getAttack() - defendingTroop.getDefense();
      if (damage > 0) {
        if (attackingTroop.getHp() > 0) {
          defendingTroop.setHp(defendingTroop.getHp() - damage);
        }
      } else {
        defendingTroop.setHp(defendingTroop.getHp() - 1);
      }

      int fireBackDamage = defendingTroop.getAttack() - attackingTroop.getDefense();
      if (fireBackDamage > 0) {
        if (defendingTroop.getHp() > 0) {
          attackingTroop.setHp(attackingTroop.getHp() - fireBackDamage);
        }
      } else {
        attackingTroop.setHp(attackingTroop.getHp() - 1);
      }
    }
    // after checking who won , will add losing troops in a list
    if (attackingTroop.getHp() <= 0) {
      troopService.deleteTroopById(attackingTroop.getId());
    } else if (defendingTroop.getHp() <= 0) {
      troopService.deleteTroopById(defendingTroop.getId());
    }
  }
}
// Todo We need an endpoint where a kingdom can initiate a fight against another. It should look
// like this:
//
// POST /kingdom/fight/{id}
//
// The initiating kingdom is the one that belongs to the user who is logged in. Make sure a kingdom
// is not able to fight with itself.
//
// One round looks like this:
//
/*The troops are paired together randomly from both sides. If one of the kingdom has more troops,
those ones will attack buildings.*/ // done

// After the troops are paired they will attack each other. The order of the hits is also decided
// randomly. The damage equals to the attacking troop's attack point minus the defending troop's
// defend point. If the damage is positive, than the defending troop loses that much HP, but at
// least 1. Then the defending troop becomes the attacking troop and they will also deal damage. //
// done

// Those troops that didn't have an opponent will hit random buildings, but they only hit the
// townhall if there's no more building. The damage equals to the attack point. Also the troop loses
// 10HP because they get tired.  // done

// The fight lasts until all the troops are dead, or one of the kingdoms loses the townhall. The
// winner kingdom will receive all the resources from the losing kingdom. If the losing kingdom
// loses the townhall, the game is over for it and the user is deleted from the database. //not yet
