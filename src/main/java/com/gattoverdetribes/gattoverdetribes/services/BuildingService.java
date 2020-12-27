package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;

public interface BuildingService {

  Building createBuilding(String buildingType, Kingdom kingdom);

  void save(Building building);
}