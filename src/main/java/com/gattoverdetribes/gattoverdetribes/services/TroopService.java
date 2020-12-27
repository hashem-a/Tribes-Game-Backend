package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;

public interface TroopService {

  Troop createTroopForKingdom(String troopType, Kingdom kingdom);

  Troop getTroopById(Long id);
}