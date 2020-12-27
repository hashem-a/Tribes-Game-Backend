package com.gattoverdetribes.gattoverdetribes.models.troops;

import static com.gattoverdetribes.gattoverdetribes.models.troops.TroopType.generateTroop;

import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidTroopException;

public class TroopFactory {

  public static Troop createTroop(String troopType) throws IllegalArgumentException {
    for (TroopType type : TroopType.values()) {
      try {
        if (TroopType.valueOf(troopType.toUpperCase()).equals(type)) {
          return generateTroop(type);
        }
      } catch (IllegalArgumentException e) {
        throw new InvalidTroopException("Invalid troop request");
      }
    }
    return null;
  }
}