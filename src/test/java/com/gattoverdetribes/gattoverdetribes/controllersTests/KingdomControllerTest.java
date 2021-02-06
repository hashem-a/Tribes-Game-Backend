package com.gattoverdetribes.gattoverdetribes.controllersTests;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gattoverdetribes.gattoverdetribes.services.JavaMailService;
import com.gattoverdetribes.gattoverdetribes.models.Player;
import com.gattoverdetribes.gattoverdetribes.repositories.PlayerRepository;
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
public class KingdomControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PlayerRepository playerRepository;
  @Mock
  private JavaMailService javaMailService;

  private String jwt;
  private Player player;

  @BeforeEach
  public void init() throws Exception {
    Mockito.doNothing().when(javaMailService).sendConfirmationMail(any(),any());
    String json =
        "{\"username\": \"rix\",\"email\": \"rix12345@gmail.com\","
            + "\"password\": \"rix12345\",\"kingdomname\": \"rix's kingdom\"}";
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk());

    player = playerRepository.findByUsername("rix").get();
    player.setActive(true);
    playerRepository.save(player);

    String user = "{\"username\": \"rix\",\"password\": \"rix12345\"}";
    MvcResult result =
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(user))
            .andExpect(status().isOk())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    JSONObject jsonObject = new JSONObject(response);
    jwt = jsonObject.get("token").toString();
  }

  @Test
  public void getKingdoms_WithKingdoms() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/kingdom/kingdoms")
                .header("X-tribes-token", "Bearer " + jwt))
        .andDo(print())
        .andExpect(status().isOk());
  }
}