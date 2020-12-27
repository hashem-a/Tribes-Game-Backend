package com.gattoverdetribes.gattoverdetribes.servicesTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.gattoverdetribes.gattoverdetribes.dtos.LoginRequestDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.LoginResponseDTO;
import com.gattoverdetribes.gattoverdetribes.dtos.RegisterRequestDTO;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.repositories.PlayerRepository;
import com.gattoverdetribes.gattoverdetribes.services.PlayerService;
import com.gattoverdetribes.gattoverdetribes.services.PlayerServiceImpl;
import java.util.Objects;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerServiceTest {

  @Autowired
  private PlayerService playerService;
  @Autowired
  private PlayerServiceImpl playerServiceImpl;
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Test
  public void registerPlayerSuccessUnitTest() {
    RegisterRequestDTO registerRequestDTO =
        new RegisterRequestDTO("rix", "rix12345", "rix's kingdomname");

    Assert.assertNull(playerService.validateRegistrationInputs(registerRequestDTO));
  }

  @Test
  public void registerPlayerWithoutNameUnitTest() {
    RegisterRequestDTO registerRequestDTO =
        new RegisterRequestDTO("", "rix12345", "rix's kingdomname");
    String message =
        Objects.requireNonNull(
            playerService.validateRegistrationInputs(registerRequestDTO).getBody())
            .getMessage();

    Assert.assertEquals("Username is required.", message);
  }

  @Test
  public void loginPlayerSuccessUnitTest() {
    Player player = new Player("rix", "rix12345");
    player.setPassword(passwordEncoder.encode("rix12345"));
    playerRepository.save(player);
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("rix", "rix12345");
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
    loginResponseDTO.setStatus("ok");

    Assert.assertEquals(
        loginResponseDTO.getStatus(),
        playerService.loginPlayer(loginRequestDTO).getBody().getStatus());
  }

  @Test
  public void loginWithWrongPasswordUnitTest() {
    Player player = new Player("rix", "rix12345");
    player.setPassword(passwordEncoder.encode("rix12345"));
    playerRepository.save(player);
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("rix", "rix1234");
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
    loginResponseDTO.setStatus("error");

    Assert.assertEquals(
        loginResponseDTO.getStatus(),
        playerService.loginPlayer(loginRequestDTO).getBody().getStatus());
  }

  @Test
  public void loginWithMissingPasswordAndUsernameUnitTest() {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("", "");
    LoginResponseDTO loginResponseDTO =
        new LoginResponseDTO("error", "Missing parameters: Username & password");
    String actualMessage =
        Objects.requireNonNull(playerServiceImpl.validateLoginInputs(loginRequestDTO).getBody())
            .getMessage();
    String expectedMessage = loginResponseDTO.getMessage();

    Assert.assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void generateTokenResponseWithJwtTokenUnitTest() {
    Player player = new Player("rix", "rix12345");
    playerRepository.save(player);

    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("rix", "rix12345");
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
    loginResponseDTO.setStatus("ok");
    String actualMessage =
        playerServiceImpl.generateTokenResponse(loginRequestDTO).getBody().getStatus();
    String expectedMessage = loginResponseDTO.getStatus();

    Assert.assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void createPlayerMatchesEncodedPasswordTest() {
    RegisterRequestDTO registerRequestDTO =
        new RegisterRequestDTO("rix", "rix12345", "rix's kingdom");
    Kingdom kingdom = new Kingdom(registerRequestDTO.getKingdomName());
    Player actualPlayer =
        playerService.createPlayer(
            registerRequestDTO.getUsername(), registerRequestDTO.getPassword(), kingdom);
    boolean matches =
        passwordEncoder.matches(registerRequestDTO.getPassword(), actualPlayer.getPassword());
    assertTrue(matches);
  }

  @Test
  public void createPlayerUnMatchingEncodedPasswordTest() {
    Player player = new Player("rix", "rix12345");
    playerRepository.save(player);
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("rix", "rix12345");
    var loginResponse = playerService.loginPlayer(loginRequestDTO);

    boolean matches = passwordEncoder.matches(loginRequestDTO.getPassword(), player.getPassword());
    assertFalse(matches);
  }

  @Test
  public void loginWithNonEncodedPasswordInDBUnitTest() {
    Player player = new Player("rix", "rix12345");
    playerRepository.save(player);
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("rix", "rix12345");
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
    loginResponseDTO.setStatus("ok");

    Assert.assertNotEquals(
        loginResponseDTO.getStatus(),
        playerService.loginPlayer(loginRequestDTO).getBody().getStatus());
  }
}