package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gattoverdetribes.gattoverdetribes.dtos.CreateBuildingRequestDTO;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.ResourceRepository;
import com.gattoverdetribes.gattoverdetribes.services.BuildingService;
import com.gattoverdetribes.gattoverdetribes.services.StarterPackService;
import java.util.Collections;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingServiceTest {

  @Mock
  private BuildingRepository buildingRepository;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private ResourceRepository resourceRepository;
  @Autowired
  private KingdomRepository kingdomRepository;
  @Autowired
  private StarterPackService starterPackService;

  private CreateBuildingRequestDTO createBuildingRequestDTO;
  private Kingdom kingdom;
  private Player player;

  @BeforeEach
  public void setup() {
    kingdom = new Kingdom("rix's kingdom");
    player = new Player("rix12345", "rix12345", kingdom);
    createBuildingRequestDTO = new CreateBuildingRequestDTO();
    kingdomRepository.save(kingdom);
    kingdom.setResources(starterPackService.setStartingResources(kingdom));
  }


  @Test
  public void testSaveBuilding() {
    Building farm1 = buildBuilding("farm");
    when(buildingRepository.findAll()).thenReturn(Collections.singletonList(farm1));
    assertTrue(buildingRepository.findAll().contains(farm1));
    assertThat(farm1).extracting(Building::getType).isEqualTo(BuildingType.FARM);
  }

  @Test
  public void testCreateCorrectTypeBuilding() throws IllegalArgumentException {
    Building farm = buildBuilding("farm");
    Building academy = buildBuilding("academy");
    Building townhall = buildBuilding("townhall");
    Building mine = buildBuilding("mine");
    Assert.assertEquals(BuildingType.FARM, farm.getType());
    Assert.assertEquals(BuildingType.ACADEMY, academy.getType());
    Assert.assertEquals(BuildingType.TOWNHALL, townhall.getType());
    Assert.assertEquals(BuildingType.MINE, mine.getType());
  }

  @Test
  public void testBuildBuildingThrowsIllegalArgumentException() throws IllegalArgumentException {
    assertThatExceptionOfType(InvalidBuildingException.class)
        .isThrownBy(() -> buildBuilding("frrm"))
        .withMessage("Invalid building request");
  }

  @Test
  public void testCreateBuildingPass() {
    Building building = mock(Building.class);
    Kingdom kingdom = mock(Kingdom.class);
    doCallRealMethod().when(building).setLevel(any(Integer.class));
    doCallRealMethod().when(building).setKingdom(any(Kingdom.class));
    building.setKingdom(kingdom);
    building.setLevel(0);
    verify(building, times(1)).setLevel(0);
    verify(building, times(1)).setKingdom(kingdom);
  }

  @Test
  public void validateBuildingTypeTest_406() {
    setup();
    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
    String message = "Invalid building type.";
    assertEquals(status, buildingService.validateBuildingType("barracks").getStatusCode());
    assertThat(buildingService.validateBuildingType("barracks").toString().equals(message));
  }

  @Test
  public void validateBuildingTypeTest_406_townhall() {
    setup();
    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
    String message = "Only one townhall per kingdom is allowed.";
    assertEquals(status, buildingService.validateBuildingType("townhall").getStatusCode());
    assertThat(buildingService.validateBuildingType("townhall").toString().equals(message));
  }

  @Test
  public void validateBuildingTypeTest_400() {
    setup();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = "Missing parameter(s): type!";
    assertEquals(status, buildingService.validateBuildingType("").getStatusCode());
    assertThat(buildingService.validateBuildingType("").toString().equals(message));
  }

  @Test
  public void purchaseBuildingTest_200() {
    setup();
    HttpStatus status = HttpStatus.OK;
    String message = "{\n"
        + "    \"id\": 14,\n"
        + "    \"type\": \"mine\",\n"
        + "    \"level\": 1,\n"
        + "    \"hp\": 1000,\n"
        + "    \"startedAt\": 1608216943596000,\n"
        + "    \"finishedAt\": 1608216943596060\n"
        + "}";
    assertEquals(status,
        buildingService.purchaseBuilding("mine", player).getStatusCode());
    assertThat(buildingService.purchaseBuilding("mine", player).toString().equals(message));
  }
}
