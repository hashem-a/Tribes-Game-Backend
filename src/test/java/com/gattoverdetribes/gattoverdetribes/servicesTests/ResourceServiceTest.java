package com.gattoverdetribes.gattoverdetribes.servicesTests;

import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.resources.Food;
import com.gattoverdetribes.gattoverdetribes.models.resources.Gold;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;
import com.gattoverdetribes.gattoverdetribes.repositories.ResourceRepository;
import com.gattoverdetribes.gattoverdetribes.services.ResourceService;
import java.util.ArrayList;
import java.util.List;
import com.gattoverdetribes.gattoverdetribes.models.resources.ResourceFactory;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ResourceServiceTest {

  @Autowired
  private ResourceService resourceService;
  @Autowired
  private ResourceRepository resourceRepository;

  Resource gold;
  Resource food;
  Kingdom kingdom;

  @BeforeEach
  public void setup() {
    kingdom = new Kingdom("Narnia");
    gold = new Gold();
    food = new Food();
    List<Resource> resources = new ArrayList<>();
    resources.add(gold);
    resources.add(food);
    resourceRepository.save(gold);
    resourceRepository.save(food);
    kingdom.setResources(resources);
  }

  @Test
  public void findAllResourcesIntegrationTest() {
    List<Resource> expectedList = resourceRepository.findAll();
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
}