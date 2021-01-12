package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;

import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.resources.Food;
import com.gattoverdetribes.gattoverdetribes.models.resources.Gold;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;
import com.gattoverdetribes.gattoverdetribes.models.resources.ResourceFactory;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.ResourceRepository;
import com.gattoverdetribes.gattoverdetribes.services.ResourceService;
import com.gattoverdetribes.gattoverdetribes.services.StarterPackService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ResourceServiceTest {

  @Autowired
  private ResourceService resourceService;
  @Autowired
  private ResourceRepository resourceRepository;
  @Autowired
  private KingdomRepository kingdomRepository;
  @Autowired
  private StarterPackService starterPackService;
  @Autowired
  private BuildingRepository buildingRepository;

  Resource gold;
  Resource food;
  Kingdom kingdom;
  Building farm;
  Building mine;
  Building townhall;
  Player player;

  @BeforeEach
  public void setup() {
    kingdom = new Kingdom("Narnia");
    kingdomRepository.save(kingdom);
    gold = new Gold();
    food = new Food();
    gold.setKingdom(kingdom);
    food.setKingdom(kingdom);
    resourceRepository.save(gold);
    resourceRepository.save(food);
    List<Resource> resources = new ArrayList<>();
    resources.add(gold);
    resources.add(food);
    resourceRepository.save(gold);
    resourceRepository.save(food);
    kingdom.setResources(resources);
    farm = buildBuilding("farm");
    farm.setLevel(1);
    farm.setKingdom(kingdom);
    buildingRepository.save(farm);
    mine = buildBuilding("mine");
    mine.setLevel(1);
    mine.setKingdom(kingdom);
    buildingRepository.save(mine);
    townhall = buildBuilding("townhall");
    townhall.setLevel(1);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);
    List<Building> buildings = new ArrayList<>();
    buildings.add(farm);
    buildings.add(mine);
    buildings.add(townhall);
    kingdom.setBuildings(buildings);
    kingdomRepository.save(kingdom);
  }

  @Test
  public void findAllResourcesIntegrationTest() {
    List<Resource> expectedList = resourceRepository.findAllByKingdomId(kingdom.getId());
    List<ResourceDetailsDTO> actualList = resourceService.convertResourcesToDTOByKingdom(kingdom);
    Assert.assertEquals(expectedList.size(), actualList.size());
  }

  @Test
  public void createResource() {
    Resource food = ResourceFactory.createResource("food");
    Resource gold = ResourceFactory.createResource("gold");

    Assert.assertEquals(Food.class, food.getClass());
    Assert.assertEquals(Gold.class, gold.getClass());
  }

  @Test
  public void checkSufficientResourcesTest_withStarterPackResources() {
    kingdom = new Kingdom("rix's kingdom");
    player = new Player("rix12345", "rix12345", kingdom);
    kingdomRepository.save(kingdom);
    kingdom.setResources(starterPackService.setStartingResources(kingdom));
    Assert.assertTrue(resourceService.checkSufficientResources(kingdom));
  }

  @Test
  public void checkSufficientResourcesTest_withoutStarterPackResources() {
    kingdom = new Kingdom("rix's kingdom");
    player = new Player("rix12345", "rix12345", kingdom);
    kingdomRepository.save(kingdom);
    Assert.assertFalse(resourceService.checkSufficientResources(kingdom));
  }

  @Test
  void calculateProductionTest() {
    Optional<Kingdom> kingdomOptBefore = kingdomRepository.findById(kingdom.getId());
    int initial = kingdomOptBefore.get().getResources().get(0).getAmount();
    resourceService.calculateKingdomsProduction();
    Optional<Kingdom> kingdomOptAfter = kingdomRepository.findById(kingdom.getId());
    Assert.assertEquals(Optional.of(initial + kingdomOptAfter.get().getKingdomFoodProduction()),
        Optional.ofNullable(kingdomOptAfter.get().getResources().get(0).getAmount()));
    Assert.assertEquals(20, kingdomOptAfter.get().getKingdomFoodProduction());
    Assert.assertEquals(20, kingdomOptAfter.get().getKingdomGoldProduction());
  }
}