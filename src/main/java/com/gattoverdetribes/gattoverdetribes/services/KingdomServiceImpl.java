package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.KingdomDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;
  private final StarterPackService starterPackService;

  @Autowired
  public KingdomServiceImpl(KingdomRepository kingdomRepository,
      StarterPackServiceImpl starterPackService) {
    this.kingdomRepository = kingdomRepository;
    this.starterPackService = starterPackService;
  }

  @Override
  public List<KingdomDetailsDTO> getKingdoms() {
    List<Kingdom> kingdoms = kingdomRepository.findAll();
    return kingdoms.stream()
        .map(KingdomDetailsDTO::new)
        .collect(Collectors.toList());
  }

  @Override
  public Kingdom getById(Long id) {
    var optionalKingdom = kingdomRepository.findById(id);
    return optionalKingdom.orElse(null);
  }

  @Override
  public Kingdom getKingdomByPlayerId(Long id) {
    return kingdomRepository.findByPlayerId(id);
  }

  @Override
  public void saveKingdom(Kingdom kingdom) {
    kingdomRepository.save(kingdom);
  }

  @Override
  public Kingdom createKingdom(String kingdomName) {
    Kingdom kingdom = new Kingdom(kingdomName);
    saveKingdom(kingdom);
    kingdom.setLocation(starterPackService.generateKingdomLocation(kingdom));
    kingdom.setBuildings(starterPackService.setStartingBuildings(kingdom));
    kingdom.setResources(starterPackService.setStartingResources(kingdom));
    return kingdom;
  }
}
