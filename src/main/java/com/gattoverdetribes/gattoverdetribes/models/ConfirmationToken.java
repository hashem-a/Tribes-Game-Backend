package com.gattoverdetribes.gattoverdetribes.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_id")
  private long id;

  @Column(name = "token")
  private String token;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date")
  private Date createdDate;

  @OneToOne(targetEntity = Player.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "player_id", referencedColumnName = "id")
  private Player player;

  public ConfirmationToken() {}

  public ConfirmationToken(Player player) {
    this.player = player;
    createdDate = new Date();
    token = UUID.randomUUID().toString();
  }

  public long getId() {
    return id;
  }

  public void setId(long tokenid) {
    this.id = tokenid;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String confirmationToken) {
    this.token = confirmationToken;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}