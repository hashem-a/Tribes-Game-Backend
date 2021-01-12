package com.gattoverdetribes.gattoverdetribes.services;

import static com.gattoverdetribes.gattoverdetribes.models.troops.TroopFactory.createTroop;

import com.gattoverdetribes.gattoverdetribes.dtos.TroopDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.exceptions.IdNotFoundException;
import com.gattoverdetribes.gattoverdetribes.mappers.Mapper;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;
import com.gattoverdetribes.gattoverdetribes.repositories.TroopRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  private final TroopRepository troopRepository;
  private final Mapper mapper;

  public TroopServiceImpl(TroopRepository troopRepository, Mapper mapper) {
    this.troopRepository = troopRepository;
    this.mapper = mapper;
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

  @Override
  public List<TroopDetailsDTO> getTroopsByKingdom(Kingdom kingdom) {
    List<Troop> troops = kingdom.getTroops();
    return mapper.modelToListTroopDetailsDto(troops);
  }
}
