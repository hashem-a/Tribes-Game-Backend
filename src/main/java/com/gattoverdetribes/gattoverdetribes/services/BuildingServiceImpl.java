package com.gattoverdetribes.gattoverdetribes.services;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;

import com.gattoverdetribes.gattoverdetribes.ExternalConfig;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

  private final BuildingRepository buildingRepository;

  @Autowired
  public BuildingServiceImpl(BuildingRepository buildingRepository) {
    this.buildingRepository = buildingRepository;
  }

  @Override
  public Building createBuilding(String buildingType, Kingdom kingdom) {
    try {
      Building building = buildBuilding(buildingType);
      building.setLevel(ExternalConfig.getInstance().getBuildingStartLevel());
      building.setKingdom(kingdom);
      buildingRepository.save(building);
      return building;
    } catch (InvalidBuildingException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public void save(Building building) {
    buildingRepository.save(building);
  }
}