package com.gattoverdetribes.gattoverdetribes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestDTO {

  private String username;
  private String password;
  private String email;
  @JsonProperty("kingdomname")
  private String kingdomName;

  public RegisterRequestDTO() {
  }

  public RegisterRequestDTO(String username, String password, String email, String kingdomName) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomName = kingdomName;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}