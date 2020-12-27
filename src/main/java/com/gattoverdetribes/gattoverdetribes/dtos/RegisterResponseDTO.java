package com.gattoverdetribes.gattoverdetribes.dtos;

import com.gattoverdetribes.gattoverdetribes.models.Player;

public class RegisterResponseDTO {

  public Long id;
  public String username;
  public Long kingdomId;
  public String avatar;
  public Integer points;

  public RegisterResponseDTO() {
  }

  public RegisterResponseDTO(String username, Long kingdomId, String avatar, Integer points) {
    this.username = username;
    this.kingdomId = kingdomId;
    this.avatar = avatar;
    this.points = points;
  }

  public RegisterResponseDTO(Player player) {
    this.id = player.getId();
    this.username = player.getUsername();
    this.kingdomId = player.getKingdom().getId();
    this.avatar = player.getAvatar();
    this.points = player.getPoints();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getKingdomId() {
    return kingdomId;
  }

  public void setKingdomId(Long kingdomId) {
    this.kingdomId = kingdomId;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }
}