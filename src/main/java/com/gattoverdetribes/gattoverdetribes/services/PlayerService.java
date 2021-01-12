package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.LoginRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.LoginResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.ErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterResponseDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PlayerService {

  Player findPlayerByUsername(String username);

  Player findPlayerById(Long id);

  Player createPlayer(String name, String password, Kingdom kingdom);

  void savePlayer(Player player);

  List<Player> findAllPlayers();

  Boolean existsPlayerByUsername(String username);

  Player findPlayerByKingdomId(Long id);

  void deletePlayer(Player player);

  ResponseEntity<RegisterResponseDTO> registerPlayer(RegisterRequestDTO registerRequestDTO);

  ResponseEntity<ErrorResponseDTO> validateRegistrationInputs(
      RegisterRequestDTO registerRequestDTO);

  ResponseEntity<LoginResponseDTO> loginPlayer(LoginRequestDTO loginRequestDTO);

  Player extractPlayerFromToken(String token);
}
