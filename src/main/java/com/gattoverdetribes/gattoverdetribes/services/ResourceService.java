package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;

public interface ResourceService {

  Resource createResource(String resourceType, Kingdom kingdom);

  void saveResource(Resource resource);

  ResponseEntity<List<ResourceDetailsDTO>> getResourcesByKingdom(Kingdom kingdom);

  List<ResourceDetailsDTO> convertResourcesToDTOByKingdom(Kingdom kingdom);

  Boolean checkSufficientResources(Kingdom kingdom);

  Boolean canPurchaseBuildingUpgrade(Kingdom kingdom);

  void calculateKingdomsProduction();

  Resource getResource(Kingdom kingdom, String type);
}
