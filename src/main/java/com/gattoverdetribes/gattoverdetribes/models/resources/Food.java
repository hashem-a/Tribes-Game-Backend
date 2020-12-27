package com.gattoverdetribes.gattoverdetribes.models.resources;

import com.gattoverdetribes.gattoverdetribes.ExternalConfig;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import javax.persistence.Entity;

@Entity
public class Food extends Resource {

  public Food() {
    this.amount = Long.valueOf(ExternalConfig.getInstance().getResourceStartFood());
  }

  public Food(Long amount) {
    this.amount = amount;
  }

  public Food(Long amount, Kingdom kingdom) {
    this.amount = amount;
    this.kingdom = kingdom;
  }
}
