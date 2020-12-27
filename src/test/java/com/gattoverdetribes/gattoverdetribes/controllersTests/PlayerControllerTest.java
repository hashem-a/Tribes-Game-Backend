package com.gattoverdetribes.gattoverdetribes.controllersTests;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gattoverdetribes.gattoverdetribes.controllers.PlayerController;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PlayerRepository playerRepository;
  @InjectMocks
  private PlayerController playerController;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Test
  public void registerSuccessfulTest() throws Exception {
    String json =
        "{\"username\": \"rix\",\"password\": \"rix123456\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("rix")))
        .andDo(print());
  }

  @Test
  public void registerWithoutNameTest() throws Exception {
    String json =
        "{\"username\": \"\",\"password\": \"rix12356\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().string("{\"status\":\"error\""
                + ",\"message\":\"Username is required.\"}"))
        .andDo(print());
  }

  @Test
  public void registerWithoutPasswordTest() throws Exception {
    String json = "{\"username\": \"rix\",\"password\": \"\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().string("{\"status\":\"error\"" + ""
                + ",\"message\":\"Password is required.\"}"))
        .andDo(print());
  }

  @Test
  public void registerWithoutKingdomNameTest() throws Exception {
    String json = "{\"username\": \"rix\",\"password\": \"rix123456\",\"kingdomname\": \"\"}";
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string("{\"status\":\"error\"" + ""
                    + ",\"message\":\"Kingdom name is required.\"}"))
        .andDo(print());
  }

  @Test
  public void registerWithAlreadyExistingUsernameTest() throws Exception {
    Player player = new Player("rix", "rix123456");
    playerRepository.save(player);
    String json =
        "{\"username\": \"rix\",\"password\": \"rix123456\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict())
        .andExpect(
            content()
                .string("{\"status\":\"error\"" + ""
                    + ",\"message\":\"Username is already taken.\"}"))
        .andDo(print());
  }

  @Test
  public void registerShortPasswordTest() throws Exception {
    String json =
        "{\"username\": \"rix\",\"password\": \"rix123\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNotAcceptable())
        .andExpect(
            content()
                .string(
                    "{\"status\":\"error\"" + ""
                        + ",\"message\":\"Password must be 8 characters.\"}"))
        .andDo(print());
  }

  @Test
  public void loginSuccessfulTest() throws Exception {
    Player player = new Player("rix", "rix12345");
    player.setPassword(passwordEncoder.encode("rix12345"));
    playerRepository.save(player);

    String json = "{\"username\": \"rix\",\"password\": \"rix12345\"}";
    MvcResult result =
        mockMvc
            .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    String jwt = result.getResponse().getContentAsString();
    System.out.println(jwt);
  }

  @Test
  public void loginWithNonExistingPlayerTest() throws Exception {
    String json = "{\"username\": \"rix\",\"password\": \"rix12345\"}";
    mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnauthorized())
        .andExpect(
            content()
                .string(
                    "{\"status\":\"error\""
                        + ",\"message\":\"Username or password is incorrect.\"}"))
        .andDo(print());
  }

  @Test
  public void loginWithWrongPasswordTest() throws Exception {
    Player player = new Player("rix", "rix12345");
    playerRepository.save(player);
    String json = "{\"username\": \"rix\",\"password\": \"rix123456\"}";
    mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnauthorized())
        .andExpect(
            content()
                .string(
                    "{\"status\":\"error\""
                        + ",\"message\":\"Username or password is incorrect.\"}"))
        .andDo(print());
  }

  @Test
  public void loginWithMissingUsernameAndPasswordTest() throws Exception {
    String json = "{}";
    mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string(
                    "{\"status\":\"error\""
                        + ",\"message\":\"Missing parameters: Username & password\"}"))
        .andDo(print());
  }

  @Test
  public void loginWithMissingUsernameTest() throws Exception {
    Player player = new Player("rix", "rix12345");
    playerRepository.save(player);
    String json = "{\"username\": \"\",\"password\": \"rix12345\"}";
    mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string("{\"status\":\"error\"" + ""
                    + ",\"message\":\"Missing parameter: Username\"}"))
        .andDo(print());
  }

  @Test
  public void loginWithoutPasswordTest() throws Exception {
    String json = "{\"username\": \"rix\",\"password\": \"\"}";
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string("{\"status\":\"error\"" + ""
                    + ",\"message\":\"Missing parameter: password\"}"))
        .andDo(print());
  }
}
