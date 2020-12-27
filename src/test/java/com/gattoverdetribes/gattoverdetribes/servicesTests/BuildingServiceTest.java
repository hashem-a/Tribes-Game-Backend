package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingFactory.buildBuilding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidBuildingException;
import com.gattoverdetribes.gattoverdetribes.models.buildings.Building;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.buildings.BuildingType;
import com.gattoverdetribes.gattoverdetribes.repositories.BuildingRepository;
import com.gattoverdetribes.gattoverdetribes.services.BuildingServiceImpl;
import java.util.Collections;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BuildingServiceTest {

  @Mock
  BuildingRepository buildingRepository;
  @InjectMocks
  BuildingServiceImpl buildingService;

  Building farm;
  Building mine;

  @BeforeEach
  public void setUp() throws InvalidBuildingException {
    farm = buildBuilding("farm");
    farm.setLevel(0);
    buildingRepository.save(farm);
    mine = buildBuilding("mine");
    mine.setLevel(0);
    buildingRepository.save(mine);
  }

  @AfterEach
  public void tearDown() {
    buildingRepository.deleteAll();
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
}