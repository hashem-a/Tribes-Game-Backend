package com.gattoverdetribes.gattoverdetribes.controllers;

import com.gattoverdetribes.gattoverdetribes.dtos.LoginRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.LoginResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterErrorResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterRequestDTO;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

  private final PlayerService playerService;

  @Autowired
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerPlayer(@RequestBody RegisterRequestDTO registerRequestDTO) {
    ResponseEntity<RegisterErrorResponseDTO> validateRegistration =
        playerService.validateRegistrationInputs(registerRequestDTO);
    if (validateRegistration == null) {
      return playerService.registerPlayer(registerRequestDTO);
    } else {
      return validateRegistration;
    }
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> loginPlayer(
      @RequestBody LoginRequestDTO loginRequestDTO) {
    return playerService.loginPlayer(loginRequestDTO);
  }
}
