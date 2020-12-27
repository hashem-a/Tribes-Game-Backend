package com.gattoverdetribes.gattoverdetribes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;

public class ResourceDetailsDTO {

  private String type;
  private Long amount;
  private Long generation;
  @JsonIgnore
  private Kingdom kingdom;

  public ResourceDetailsDTO() {
  }

  public ResourceDetailsDTO(Resource resource) {
    this.type = resource.getClass().getSimpleName().toLowerCase();
    this.amount = resource.getAmount();
    this.generation = 0L;
    this.kingdom = resource.getKingdom();
  }

  public ResourceDetailsDTO(String type, Long amount, Long generation, Kingdom kingdom) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.kingdom = kingdom;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public Long getGeneration() {
    return generation;
  }

  public void setGeneration(Long generation) {
    this.generation = generation;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }
}