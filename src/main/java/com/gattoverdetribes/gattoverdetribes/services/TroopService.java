package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.TroopDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;
import java.util.List;

public interface TroopService {

  Troop createTroopForKingdom(String troopType, Kingdom kingdom);

  Troop getTroopById(Long id);

  List<TroopDetailsDTO> getTroopsByKingdom(Kingdom kingdom);
}