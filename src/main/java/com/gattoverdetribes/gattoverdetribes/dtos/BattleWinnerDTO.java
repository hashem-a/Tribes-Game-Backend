package com.gattoverdetribes.gattoverdetribes.dtos;

import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;
import java.util.List;

public class BattleWinnerDTO {
  private String name;
  private List<Resource> resourcesBeforeBattle;
  private List<Resource> resourcesGainedAfterBattle;

  public BattleWinnerDTO() {
  }

  public BattleWinnerDTO(String name,
      List<Resource> resourcesBeforeBattle,
      List<Resource> resourcesGainedAfterBattle) {
    this.name = name;
    this.resourcesBeforeBattle = resourcesBeforeBattle;
    this.resourcesGainedAfterBattle = resourcesGainedAfterBattle;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Resource> getResourcesBeforeBattle() {
    return resourcesBeforeBattle;
  }

  public void setResourcesBeforeBattle(
      List<Resource> resourcesBeforeBattle) {
    this.resourcesBeforeBattle = resourcesBeforeBattle;
  }

  public List<Resource> getResourcesGainedAfterBattle() {
    return resourcesGainedAfterBattle;
  }

  public void setResourcesGainedAfterBattle(
      List<Resource> resourcesGainedAfterBattle) {
    this.resourcesGainedAfterBattle = resourcesGainedAfterBattle;
  }
}
