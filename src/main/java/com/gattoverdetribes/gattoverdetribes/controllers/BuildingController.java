package com.gattoverdetribes.gattoverdetribes.controllers;

import com.gattoverdetribes.gattoverdetribes.dtos.CreateBuildingRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.ErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.services.BuildingService;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import com.gattoverdetribes.gattoverdetribes.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.gattoverdetribes.gattoverdetribes.dtos.BuildingDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom")
public class BuildingController {

  private final BuildingService buildingService;
  private final PlayerService playerService;
  private final ResourceService resourceService;

  @Autowired
  public BuildingController(
      BuildingService buildingService,
      PlayerService playerService,
      ResourceService resourceService) {
    this.buildingService = buildingService;
    this.playerService = playerService;
    this.resourceService = resourceService;
  }

  @GetMapping("/buildings")
  public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByKingdom(
      @RequestHeader(name = "X-tribes-token") String token) {
    Kingdom kingdom = playerService.extractPlayerFromToken(token).getKingdom();
    return buildingService.getBuildingsByKingdom(kingdom);
  }

  @PostMapping("/buildings")
  public ResponseEntity<?> createBuilding(@RequestHeader(name = "X-tribes-token") String token,
      @RequestBody CreateBuildingRequestDTO createBuildingRequestDTO) {

    Player player = playerService.extractPlayerFromToken(token);
    ResponseEntity<?> validateBuildingType = buildingService
        .validateBuildingType(createBuildingRequestDTO.getType());

    if (validateBuildingType == null) {
      if (resourceService.checkSufficientResources(player.getKingdom())) {
        return buildingService.purchaseBuilding(createBuildingRequestDTO.getType(), player);
      }
      return ResponseEntity.status(409)
          .body(new ErrorResponseDTO("Not enough resource."));
    }
    return validateBuildingType;
  }

  @PostMapping("/buildings/{id}")
  public ResponseEntity<?> upgradeBuilding(@RequestHeader(name = "X-tribes-token") String token,
      @PathVariable Long id) {
    Player player = playerService.extractPlayerFromToken(token);
    return buildingService.upgradeBuilding(player, id);
  }
}
