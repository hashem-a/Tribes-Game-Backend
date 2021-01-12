package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.dtos.BuildingDetailsDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface BuildingService {

  Building createBuilding(String buildingType, Kingdom kingdom);

  ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByKingdom(Kingdom kingdom);

  ResponseEntity<?> validateBuildingType(String type);

  ResponseEntity<?> purchaseBuilding(String type, Player player);

  void save(Building building);

  ResponseEntity<?> upgradeBuilding(Player player, Long id);

  void upgradeLevel(Building building);

  Boolean isBuildingUpgradeable(Building building);
}