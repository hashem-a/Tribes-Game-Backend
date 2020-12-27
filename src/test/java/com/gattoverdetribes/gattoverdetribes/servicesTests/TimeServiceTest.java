package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gattoverdetribes.gattoverdetribes.models.resources.GameTime;
import com.gattoverdetribes.gattoverdetribes.repositories.GameTimeRepository;
import com.gattoverdetribes.gattoverdetribes.services.TimeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class TimeServiceTest {

  @Autowired
  private TimeServiceImpl timeServiceimpl;
  @Autowired
  private GameTimeRepository gameTimeRepository;

  @Test
  public void checkTime_increaseTick() throws InterruptedException {
    timeServiceimpl.checkTime();
    GameTime gameTime = gameTimeRepository.findFirstByOrderByIdAsc();
    long initialTick = gameTime.getTicks();
    Thread.sleep(5000);
    GameTime gameTime2 = gameTimeRepository.findFirstByOrderByIdAsc();
    long tickAfterOnePeriod = gameTime2.getTicks();
    assertEquals(tickAfterOnePeriod, initialTick + 1L);
  }

  @Test
  public void checkTime_notIncreaseTick() throws InterruptedException {
    timeServiceimpl.checkTime();
    GameTime gameTime = gameTimeRepository.findFirstByOrderByIdAsc();
    long initialTick = gameTime.getTicks();
    Thread.sleep(1000);
    GameTime gameTime2 = gameTimeRepository.findFirstByOrderByIdAsc();
    long tickAfterOnePeriod = gameTime2.getTicks();
    assertEquals(tickAfterOnePeriod, initialTick);
  }

  @Test
  public void getTime_ReturnsCurrentTimeInLong() {
    assertEquals(System.currentTimeMillis(), timeServiceimpl.getTimeNow());
  }
}
