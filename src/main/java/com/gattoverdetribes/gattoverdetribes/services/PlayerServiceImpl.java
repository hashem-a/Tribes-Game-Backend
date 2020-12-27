package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.LoginRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.LoginResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterResponseDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.repositories.PlayerRepository;
import com.gattoverdetribes.gattoverdetribes.security.service.PlayerDetailsService;
import com.gattoverdetribes.gattoverdetribes.security.utilities.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final PlayerDetailsService playerDetailsService;
  private final JwtUtil jwtTokenUtil;
  private final PlayerRepository playerRepository;
  private final KingdomService kingdomService;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public PlayerServiceImpl(
      PlayerDetailsService playerDetailsService,
      JwtUtil jwtTokenUtil,
      PlayerRepository playerRepository,
      KingdomService kingdomService,
      BCryptPasswordEncoder passwordEncoder) {
    this.playerDetailsService = playerDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.playerRepository = playerRepository;
    this.kingdomService = kingdomService;
    this.passwordEncoder = passwordEncoder;
  }

  public Player checkOptionalPlayer(String username) {
    if (playerRepository.findByUsername(username).isPresent()) {
      return playerRepository.findByUsername(username).get();
    }
    return null;
  }

  @Override
  public Player createPlayer(String name, String password, Kingdom kingdom) {
    String encodedPassword = passwordEncoder.encode(password);
    Player player = new Player(name, encodedPassword, kingdom);
    player.setKingdom(kingdom);
    playerRepository.save(player);
    return player;
  }

  @Override
  public ResponseEntity<RegisterResponseDTO> registerPlayer(RegisterRequestDTO registerRequestDTO) {
    Kingdom kingdom = kingdomService.createKingdom(registerRequestDTO.getKingdomName());
    Player player =
        createPlayer(registerRequestDTO.getUsername(), registerRequestDTO.getPassword(), kingdom);
    RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(player);
    return ResponseEntity.ok().body(registerResponseDTO);
  }

  @Override
  public ResponseEntity<RegisterErrorResponseDTO> validateRegistrationInputs(
      RegisterRequestDTO registerRequestDTO) {
    RegisterErrorResponseDTO registerErrorResponseDTO = new RegisterErrorResponseDTO();

    if (isNullOrEmpty(registerRequestDTO.getUsername())) {
      registerErrorResponseDTO.setMessage("Username is required.");
      return new ResponseEntity<>(registerErrorResponseDTO, HttpStatus.BAD_REQUEST);
    } else if (isNullOrEmpty(registerRequestDTO.getPassword())) {
      registerErrorResponseDTO.setMessage("Password is required.");
      return new ResponseEntity<>(registerErrorResponseDTO, HttpStatus.BAD_REQUEST);
    } else if (isNullOrEmpty(registerRequestDTO.getKingdomName())) {
      registerErrorResponseDTO.setMessage("Kingdom name is required.");
      return new ResponseEntity<>(registerErrorResponseDTO, HttpStatus.BAD_REQUEST);
    } else if (registerRequestDTO.getPassword().length() < 8) {
      registerErrorResponseDTO.setMessage("Password must be 8 characters.");
      return new ResponseEntity<>(registerErrorResponseDTO, HttpStatus.NOT_ACCEPTABLE);
    } else if (playerRepository.existsByUsername(registerRequestDTO.getUsername())) {
      registerErrorResponseDTO.setMessage("Username is already taken.");
      return new ResponseEntity<>(registerErrorResponseDTO, HttpStatus.CONFLICT);
    } else {
      return null;
    }
  }

  @Override
  public ResponseEntity<LoginResponseDTO> loginPlayer(LoginRequestDTO loginRequestDTO) {
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

    if (isNotNullAndNotEmpty(loginRequestDTO.getUsername())
        && isNotNullAndNotEmpty(loginRequestDTO.getPassword())) {

      Player player = checkOptionalPlayer(loginRequestDTO.getUsername());
      if (player == null) {
        loginResponseDTO.setStatus("error");
        loginResponseDTO.setMessage("Username or password is incorrect.");
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.UNAUTHORIZED);
      } else if (!passwordEncoder.matches(loginRequestDTO.getPassword(), player.getPassword())) {
        loginResponseDTO.setStatus("error");
        loginResponseDTO.setMessage("Username or password is incorrect.");
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.UNAUTHORIZED);
      } else {
        return generateTokenResponse(loginRequestDTO);
      }
    } else {
      return validateLoginInputs(loginRequestDTO);
    }
  }

  public ResponseEntity<LoginResponseDTO> generateTokenResponse(LoginRequestDTO loginRequestDTO) {
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
    final UserDetails userDetails =
        playerDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
    final String jwt = jwtTokenUtil.generateToken(userDetails);
    loginResponseDTO.setStatus("ok");
    loginResponseDTO.setToken(jwt);
    return ResponseEntity.ok().body(loginResponseDTO);
  }

  public ResponseEntity<LoginResponseDTO> validateLoginInputs(LoginRequestDTO loginRequestDTO) {
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

    if (isNullOrEmpty(loginRequestDTO.getUsername())
        && isNullOrEmpty(loginRequestDTO.getPassword())) {
      loginResponseDTO.setStatus("error");
      loginResponseDTO.setMessage("Missing parameters: Username & password");
    } else if (isNullOrEmpty(loginRequestDTO.getUsername())) {
      loginResponseDTO.setStatus("error");
      loginResponseDTO.setMessage("Missing parameter: Username");
    } else if (isNullOrEmpty(loginRequestDTO.getPassword())) {
      loginResponseDTO.setStatus("error");
      loginResponseDTO.setMessage("Missing parameter: password");
    }
    return new ResponseEntity<>(loginResponseDTO, HttpStatus.BAD_REQUEST);
  }

  private boolean isNotNullAndNotEmpty(String str) {
    return str != null && !str.isEmpty();
  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }

  @Override
  public Player extractPlayerFromToken(String token) {
    String subToken = token.substring(7);
    String playerName = jwtTokenUtil.extractUsername(subToken);
    return findPlayerByUsername(playerName);
  }

  @Override
  public Player findPlayerByUsername(String username) {
    return playerRepository.findByUsername(username).orElse(null);
  }

  @Override
  public Player findPlayerById(Long id) {
    return playerRepository.findById(id).orElse(null);
  }

  @Override
  public void savePlayer(Player player) {
    playerRepository.save(player);
  }

  @Override
  public List<Player> findAllPlayers() {
    return playerRepository.findAll();
  }

  @Override
  public Boolean existsPlayerByUsername(String username) {
    return playerRepository.existsByUsername(username);
  }

  @Override
  public void deletePlayer(Player player) {
    if (playerRepository.findByUsername(player.getUsername()).isPresent()) {
      playerRepository.delete(player);
    }
  }

  @Override
  public Player findPlayerByKingdomId(Long id) {
    return playerRepository.findPlayerByKingdomId(id);
  }
}
