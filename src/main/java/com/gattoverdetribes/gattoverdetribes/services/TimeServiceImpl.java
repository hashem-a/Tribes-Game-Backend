package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.models.resources.GameTime;
import com.gattoverdetribes.gattoverdetribes.repositories.GameTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@EnableScheduling
public class TimeServiceImpl implements TimeService, CommandLineRunner {

  private final GameTimeRepository gameTimeRepository;

  @Autowired
  public TimeServiceImpl(
      GameTimeRepository gameTimeRepository) {
    this.gameTimeRepository = gameTimeRepository;
  }

  @Scheduled(cron = "${cron.expression}")
  public void serverTick() {
    checkTime();
  }

  @Override
  public void checkTime() {
    GameTime gameTime = gameTimeRepository.findFirstByOrderByIdAsc();
    long ticks = gameTime.getTicks() + 1;
    long serverStart = gameTime.getServerStart();
    long timeNow = System.currentTimeMillis() / 1000;
    long expectedTicks = (timeNow - serverStart) / 60;
    long ticksDifference = expectedTicks - ticks;
    for (int i = 0; i < ticksDifference; i++) {
      ticks++;
    }
    gameTime.setTicks(ticks);
    gameTimeRepository.save(gameTime);
    System.out.println("There were already " + gameTime.getTicks() + " ticks since start");
  }

  @Override
  public long getTimeNow() {
    return System.currentTimeMillis();
  }

  @Override
  public void run(String... args) throws Exception {
    GameTime gameTime = new GameTime();
    gameTime.setServerStart(1607501228L);
    gameTimeRepository.save(gameTime);
    long startInMilliSeconds = 1607501228000L;
    Instant instant = Instant.ofEpochMilli(startInMilliSeconds).plusSeconds(3600);
    System.out.println("Server started at: " + instant);
  }
}