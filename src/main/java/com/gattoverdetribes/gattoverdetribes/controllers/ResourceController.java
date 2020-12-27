package com.gattoverdetribes.gattoverdetribes.controllers;

import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import com.gattoverdetribes.gattoverdetribes.services.ResourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom")
public class ResourceController {

  private final ResourceService resourceService;
  private final PlayerService playerService;

  @Autowired
  public ResourceController(
      ResourceService resourceService,
      PlayerService playerService) {
    this.resourceService = resourceService;
    this.playerService = playerService;
  }

  @GetMapping("/resources")
  public ResponseEntity<List<ResourceDetailsDTO>> getResourcesByKingdom(
      @RequestHeader(name = "X-tribes-token") String token) {
    Kingdom kingdom = playerService.extractPlayerFromToken(token).getKingdom();
    return resourceService.getResourcesByKingdom(kingdom);
  }
}