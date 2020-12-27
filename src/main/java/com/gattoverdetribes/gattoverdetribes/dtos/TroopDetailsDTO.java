package com.gattoverdetribes.gattoverdetribes.dtos;

import com.gattoverdetribes.gattoverdetribes.models.troops.Troop;

public class TroopDetailsDTO {

  private Long id;
  private int level;
  private int hp;
  private int attack;
  private int defense;
  private Long startedAt;
  private Long finishedAt;

  public TroopDetailsDTO(Troop troop) {
    this.id = troop.getId();
    this.level = troop.getLevel();
    this.hp = troop.getHp();
    this.attack = troop.getAttack();
    this.defense = troop.getDefense();
    this.startedAt = troop.getStartedAt();
    this.finishedAt = troop.getFinishedAt();
  }

  public TroopDetailsDTO() {
  }

  public TroopDetailsDTO(Long id, int level, int hp, int attack, int defense,
      Long startedAt, Long finishedAt) {
    this.id = id;
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defense = defense;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public int getAttack() {
    return attack;
  }

  public void setAttack(int attack) {
    this.attack = attack;
  }

  public int getDefense() {
    return defense;
  }

  public void setDefense(int defense) {
    this.defense = defense;
  }

  public Long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Long startedAt) {
    this.startedAt = startedAt;
  }

  public Long getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(Long finishedAt) {
    this.finishedAt = finishedAt;
  }
}