package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import com.gattoverdetribes.gattoverdetribes.services.BuildingService;
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
public class BuildingServiceIntegrationTest {

  @Autowired
  BuildingRepository buildingRepository;
  @Autowired
  BuildingService buildingService;
  @Autowired
  KingdomRepository kingdomRepository;

  Kingdom kingdom;
  Building farm;
  Building mine;
  Building townHall;

  @BeforeEach
  public void setUp() throws InvalidBuildingException {
    kingdom = new Kingdom("Narnia");
    kingdomRepository.save(kingdom);
    farm = buildBuilding("farm");
    farm.setLevel(1);
    farm.setKingdom(kingdom);
    buildingRepository.save(farm);
    mine = buildBuilding("mine");
    mine.setLevel(1);
    mine.setKingdom(kingdom);
    buildingRepository.save(mine);
    townHall = buildBuilding("townhall");
    townHall.setLevel(2);
    townHall.setKingdom(kingdom);
    buildingRepository.save(townHall);
  }

  @Test
  public void upgradeLevelTest_FarmLevelIsLowerThanTownHalls() {
    Integer levelBeforeUpgrade = farm.getLevel();
    buildingService.upgradeLevel(farm);
    buildingRepository.save(farm);
    Integer levelAfterUpgrade = farm.getLevel();

    assertNotEquals(levelBeforeUpgrade, levelAfterUpgrade);
  }

  @Test
  public void upgradeLevelTest_FarmLevelIsTheSameAsTownHalls() {
    farm.setLevel(townHall.getLevel());
    Integer levelBeforeUpgrade = farm.getLevel();
    buildingService.upgradeLevel(farm);
    buildingRepository.save(farm);
    Integer levelAfterUpgrade = farm.getLevel();

    assertEquals(levelBeforeUpgrade, levelAfterUpgrade);
  }

  @Test
  public void upgradeLevelTest_upgradeTownHallLevel() {
    Integer levelBeforeUpgrade = townHall.getLevel();
    buildingService.upgradeLevel(townHall);
    buildingRepository.save(townHall);
    Integer levelAfterUpgrade = townHall.getLevel();

    assertNotEquals(levelBeforeUpgrade, levelAfterUpgrade);
  }

  @Test
  public void checkMineLevelToTownHallOkTest() {

    assertEquals(true, buildingService.isBuildingUpgradeable(mine));
  }

  @Test
  public void checkFarmLevelToTownHallFailedTest() {
    farm.setLevel(2);
    buildingRepository.save(farm);

    assertEquals(false, buildingService.isBuildingUpgradeable(farm));
  }

  @Test
  public void checkTownHallLevelOkTest() {

    assertEquals(true, buildingService.isBuildingUpgradeable(townHall));
  }

  @Test
  public void checkTownHallLevelFailedTest() {
    townHall.setLevel(20);
    buildingRepository.save(townHall);

    assertEquals(false, buildingService.isBuildingUpgradeable(townHall));
  }
}
