package com.gattoverdetribes.gattoverdetribes.models.buildings;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType.generateBuilding;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;

public class BuildingFactory {

  public static Building buildBuilding(String buildingType) throws IllegalArgumentException {
    for (BuildingType type : BuildingType.values()) {
      try {
        if (BuildingType.valueOf(buildingType.toUpperCase()).equals(type)) {
          return generateBuilding(type);
        }
      } catch (IllegalArgumentException e) {
        throw new InvalidBuildingException("Invalid building request");
      }
    }
    return null;
  }
}