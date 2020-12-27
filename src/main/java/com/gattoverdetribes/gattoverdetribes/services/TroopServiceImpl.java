package com.gattoverdetribes.gattoverdetribes.services;

import static com.gattoverdetribes.gattoverdetribes.models.troops.TroopFactory.createTroop;

import com.gattoverdetribes.gattoverdetribes.exceptions.IdNotFoundException;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;
import com.gattoverdetribes.gattoverdetribes.repositories.TroopRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  private final TroopRepository troopRepository;

  public TroopServiceImpl(TroopRepository troopRepository) {
    this.troopRepository = troopRepository;
  }

  @Override
  public Troop createTroopForKingdom(String troopType, Kingdom kingdom) {
    Troop troop = createTroop(troopType);
    Objects.requireNonNull(troop).setKingdom(kingdom);
    troopRepository.save(troop);
    return troop;
  }

  @Override
  public Troop getTroopById(Long id) {
    return troopRepository.findById(id).orElseThrow((IdNotFoundException::new));
  }
}