package com.gattoverdetribes.gattoverdetribes.controllersTests;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gattoverdetribes.gattoverdetribes.services.JavaMailService;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.repositories.KingdomRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.PlayerRepository;
import com.gattoverdetribes.gattoverdetribes.repositories.TroopRepository;
import com.gattoverdetribes.gattoverdetribes.services.TroopService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class TroopControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private KingdomRepository kingdomRepository;
  @Autowired
  private TroopRepository troopRepository;
  @Autowired
  private TroopService troopService;
  @Autowired
  private PlayerRepository playerRepository;
  @Mock
  private JavaMailService javaMailService;

  String jwt;
  Kingdom kingdom;
  Player player;

  @BeforeEach
  public void init() throws Exception {
    Mockito.doNothing().when(javaMailService).sendConfirmationMail(any(), any());
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"username\": \"rix\",\"password\": \"rix12345\","
                        + "\"email\": \"rix12345@gmail.com\",\"kingdomname\": "
                        + "\"rix's kingdom\"}"))
        .andExpect(status().isOk());

    player = playerRepository.findByUsername("rix").get();
    player.setActive(true);
    playerRepository.save(player);

    MvcResult result =
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"rix\",\"password\": \"rix12345\"}"))
            .andExpect(status().isOk())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    JSONObject jsonObject = new JSONObject(response);
    jwt = jsonObject.get("token").toString();
    kingdom = kingdomRepository.findByName("rix's kingdom");
  }

  @Test
  public void getTroops_WithTroops() throws Exception {
    troopService.createTroop("archer", kingdom);
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/kingdom/troops")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-tribes-token", "Bearer " + jwt))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void getTroops_WithoutTroops() throws Exception {
    troopRepository.deleteAll(troopRepository.findAll());
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get("/kingdom/troops")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-tribes-token", "Bearer " + jwt))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}