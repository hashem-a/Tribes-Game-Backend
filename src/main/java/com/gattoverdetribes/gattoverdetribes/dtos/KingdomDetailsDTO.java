package com.gattoverdetribes.gattoverdetribes.dtos;

import com.gattoverdetribes.gattoverdetribes.models.Kingdom;

public class KingdomDetailsDTO {

  private Long id;
  private String name;
  private Integer xcord;
  private Integer ycord;

  public KingdomDetailsDTO(Kingdom kingdom) {
    this.id = kingdom.getId();
    this.name = kingdom.getName();
    this.ycord = kingdom.getLocation().getYcord();
    this.xcord = kingdom.getLocation().getXcord();
  }

  public KingdomDetailsDTO(Long id, String name, Integer xcord, Integer ycord) {
    this.id = id;
    this.name = name;
    this.xcord = xcord;
    this.ycord = ycord;
  }

  public KingdomDetailsDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getXcord() {
    return xcord;
  }

  public void setXcord(Integer xcord) {
    this.xcord = xcord;
  }

  public int getYcord() {
    return ycord;
  }

  public void setYcord(Integer ycord) {
    this.ycord = ycord;
  }
}