package com.gattoverdetribes.gattoverdetribes.dtos;

import com.gattoverdetribes.gattoverdetribes.models.Player;

public class PlayerRequestDTO {

  private String username;
  private String password;
  private String kingdomName;

  public PlayerRequestDTO(String username, String password, String kingdomName) {
    this.username = username;
    this.password = password;
    this.kingdomName = kingdomName;
  }

  public PlayerRequestDTO() {
  }

  public PlayerRequestDTO(Player player) {
    this.username = player.getUsername();
    this.password = player.getPassword();
    this.kingdomName = player.getKingdom().getName();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }
}
