package com.gattoverdetribes.gattoverdetribes.services;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;

import com.gattoverdetribes.gattoverdetribes.ExternalConfig;
import com.gattoverdetribes.gattoverdetribes.dtos.BuildingDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.CreateBuildingResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.ErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.CreateBuildingErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;
import com.gattoverdetribes.gattoverdetribes.mappers.Mapper;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

  private final BuildingRepository buildingRepository;
  private final ResourceService resourceService;
  private final Mapper mapper;

  @Autowired
  public BuildingServiceImpl(
      BuildingRepository buildingRepository, ResourceService resourceService, Mapper mapper) {
    this.buildingRepository = buildingRepository;
    this.resourceService = resourceService;
    this.mapper = mapper;
  }

  public Building checkOptionalBuilding(Long id) {
    if (buildingRepository.findById(id).isPresent()) {
      return buildingRepository.findById(id).get();
    }
    return null;
  }

  @Override
  public Building createBuilding(String buildingType, Kingdom kingdom) {
    try {
      Building building = buildBuilding(buildingType);
      assert building != null;
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
  public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByKingdom(Kingdom kingdom) {
    List<Building> buildings = kingdom.getBuildings();
    List<BuildingDetailsDTO> mappedBuildings = mapper.modelToDto(buildings);

    if (mappedBuildings.isEmpty()) {
      return new ResponseEntity<>(mappedBuildings, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(mappedBuildings, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<?> purchaseBuilding(String type, Player player) {
    Building building = createBuilding(type.toUpperCase(), player.getKingdom());
    return ResponseEntity.status(200).body(mapper.buildingToCreateBuildingResponseDto(building));
  }

  @Override
  public ResponseEntity<?> validateBuildingType(String type) {
    List<BuildingType> buildingTypes = Arrays.asList(BuildingType.values());
    List<String> buildings =
        buildingTypes.stream()
            .map(type1 -> type1.toString().toLowerCase())
            .collect(Collectors.toList());
    boolean containsType = buildings.contains(type);

    if (type == null || type.isEmpty()) {
      return ResponseEntity.status(400).body(new ErrorResponseDTO("Missing parameter(s): type!"));
    } else if (!containsType) {
      return ResponseEntity.status(406).body(new ErrorResponseDTO("Invalid building type."));
    } else if (type.equals("townhall")) {
      return ResponseEntity.status(406)
          .body(new ErrorResponseDTO("Only one townhall per kingdom is allowed."));
    }
    return null;
  }

  @Override
  public void save(Building building) {
    buildingRepository.save(building);
  }

  public ResponseEntity<?> upgradeBuilding(Player player, Long id) {
    Building building = checkOptionalBuilding(id);
    Boolean checkResourceSufficiency =
        resourceService.canPurchaseBuildingUpgrade(player.getKingdom());

    if (building == null) {
      return ResponseEntity.status(404).body(new CreateBuildingErrorResponseDTO("Id not found."));
    } else if (!isBuildingUpgradeable(building)) {
      return ResponseEntity.status(406)
          .body(new CreateBuildingErrorResponseDTO("Invalid building level."));
    } else if (!checkResourceSufficiency) {
      return ResponseEntity.status(409)
          .body(new CreateBuildingErrorResponseDTO("Not enough resource."));
    } else {
      upgradeLevel(building);
      return ResponseEntity.status(200).body(mapper.buildingToCreateBuildingResponseDto(building));
    }
  }

  public void upgradeLevel(Building building) {
    Integer level = building.getLevel();

    if (isBuildingUpgradeable(building)) {
      level = level + 1;
    }
    building.setLevel(level);
    save(building);
  }

  public Boolean isBuildingUpgradeable(Building building) {
    Integer currentBuildingLevel = building.getLevel();
    Kingdom kingdom = building.getKingdom();
    List<Building> kingdomsBuildings = buildingRepository.findAllByKingdomId(kingdom.getId());
    boolean isTownHall = building.getType().equals(BuildingType.TOWNHALL);
    Integer townHallLevel = 0;

    for (Building selectedBuilding : kingdomsBuildings) {
      if (selectedBuilding.getType().equals(BuildingType.TOWNHALL)) {
        townHallLevel = selectedBuilding.getLevel();
      }
    }

    if (!isTownHall && currentBuildingLevel < townHallLevel) {
      return true;
    } else {
      return isTownHall && townHallLevel < 20;
    }
  }
}
