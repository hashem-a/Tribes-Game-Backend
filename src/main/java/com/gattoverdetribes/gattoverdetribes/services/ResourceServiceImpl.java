package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.ExternalConfig;
import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidResourceException;
import com.gattoverdetribes.gattoverdetribes.mappers.Mapper;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;
import com.gattoverdetribes.gattoverdetribes.models.resources.ResourceFactory;
import com.gattoverdetribes.gattoverdetribes.models.resources.ResourceType;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.ResourceRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;
  private final KingdomRepository kingdomRepository;
  private final Mapper mapper;

  @Autowired
  public ResourceServiceImpl(
      ResourceRepository resourceRepository, KingdomRepository kingdomRepository, Mapper mapper) {
    this.resourceRepository = resourceRepository;
    this.kingdomRepository = kingdomRepository;
    this.mapper = mapper;
  }

  @Override
  public Resource createResource(String resourceType, Kingdom kingdom) {
    try {
      Resource resource = ResourceFactory.createResource(resourceType);
      resource.setKingdom(kingdom);
      saveResource(resource);
      return resource;
    } catch (InvalidResourceException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public void saveResource(Resource resource) {
    resourceRepository.save(resource);
  }

  public ResponseEntity<List<ResourceDetailsDTO>> getResourcesByKingdom(Kingdom kingdom) {

    List<ResourceDetailsDTO> resources = convertResourcesToDTOByKingdom(kingdom);

    if (resources.isEmpty()) {
      return new ResponseEntity<>(resources, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(resources, HttpStatus.OK);
    }
  }

  @Override
  public List<ResourceDetailsDTO> convertResourcesToDTOByKingdom(Kingdom kingdom) {
    List<Resource> resources = resourceRepository.findAllByKingdomId(kingdom.getId());
    return mapper.resourceToResourceDetailsDTO(resources);
  }

  @Override
  public Boolean checkSufficientResources(Kingdom kingdom) {
    Integer buildingCost = ExternalConfig.getInstance().getBuildingConstructionCost();
    List<Resource> resources = resourceRepository.findAllByKingdomId(kingdom.getId());
    for (var resource : resources) {
      if (resource.getType() == ResourceType.GOLD) {
        if (resource.getAmount() >= buildingCost) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Boolean canPurchaseBuildingUpgrade(Kingdom kingdom) {
    Integer buildingCost = ExternalConfig.getInstance().getBuildingUpgradeCost();
    List<Resource> resources = resourceRepository.findAllByKingdomId(kingdom.getId());

    for (Resource resource : resources) {
      if (resource.getType() == ResourceType.GOLD) {
        Integer goldAmount = resource.getAmount();
        if (goldAmount >= buildingCost) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Resource getResource(Kingdom kingdom, String type) throws EntityNotFoundException {
    return kingdom.getResources().stream()
        .filter(r -> r.getType().equals(ResourceType.valueOf(type)))
        .findAny()
        .orElseThrow(() -> new EntityNotFoundException("Resource is not found!"));
  }

  @Override
  public void calculateKingdomsProduction() {
    if (kingdomRepository.findAll().size() == 0) {
      System.err.println("No kingdom found, create one please");
    } else {
      for (Kingdom kingdom : kingdomRepository.findAll()) {
        setKingdomProduction(kingdom);
        if (kingdom.getResources().size() < 2) {
          System.err.println("One or both resources missing, i can count only to potato");
        } else {
          List<Resource> resources = kingdom.getResources();
          setResourcesAmount(kingdom, resources);
          resourceRepository.saveAll(resources);
        }
      }
    }
  }

  private void setKingdomProduction(Kingdom kingdom) {
    int goldProduction = 0;
    int foodProduction = 0;
    if (kingdom.getBuildings().size() == 0) {
      System.err.println("No buildings, no production :(");
    }
    for (Building building : kingdom.getBuildings()) {
      if (building.getType() == BuildingType.MINE) {
        goldProduction += building.getGoldProduction();
      } else if (building.getType() == BuildingType.FARM) {
        foodProduction += building.getFoodProduction();
      } else if (building.getType() == BuildingType.TOWNHALL) {
        kingdom.setMaxStorage(building.getLevel() * 1000);
        goldProduction += building.getGoldProduction();
        foodProduction += building.getFoodProduction();
      }
      kingdom.setKingdomFoodProduction(foodProduction - kingdom.getTroops().size());
      kingdom.setKingdomGoldProduction(goldProduction);
      kingdomRepository.save(kingdom);
    }
  }

  private void setResourcesAmount(Kingdom kingdom, List<Resource> resources) {
    for (var resource : resources) {
      if (resource.getType() == ResourceType.FOOD) {
        if ((kingdom.getKingdomFoodProduction() + resource.getAmount()) > kingdom.getMaxStorage()) {
          resource.setAmount(kingdom.getMaxStorage());
        }
        resource.setAmount(kingdom.getKingdomFoodProduction() + resource.getAmount());
      } else if (resource.getType() == ResourceType.GOLD) {
        if ((kingdom.getKingdomGoldProduction() + resource.getAmount()) > kingdom.getMaxStorage()) {
          resource.setAmount(kingdom.getMaxStorage());
        }
        resource.setAmount(kingdom.getKingdomGoldProduction() + resource.getAmount());
      }
    }
  }
}
