package com.gattoverdetribes.gattoverdetribes.dtos;

public class TokenValidationSuccessfulDTO {

  private String message;

  public TokenValidationSuccessfulDTO() {
  }

  public TokenValidationSuccessfulDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
