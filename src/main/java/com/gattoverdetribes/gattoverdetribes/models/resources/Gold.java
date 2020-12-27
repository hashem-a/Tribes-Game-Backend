package com.gattoverdetribes.gattoverdetribes.models.resources;

import com.gattoverdetribes.gattoverdetribes.ExternalConfig;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import javax.persistence.Entity;

@Entity
public class Gold extends Resource {

  public Gold() {
    this.amount = Long.valueOf(ExternalConfig.getInstance().getResourceStartGold());
  }

  public Gold(Long amount) {
    this.amount = amount;
  }

  public Gold(Long amount, Kingdom kingdom) {
    this.amount = amount;
    this.kingdom = kingdom;
  }
}
