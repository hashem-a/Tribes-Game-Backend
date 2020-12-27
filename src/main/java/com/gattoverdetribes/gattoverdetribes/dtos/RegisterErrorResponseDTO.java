package com.gattoverdetribes.gattoverdetribes.dtos;

public class RegisterErrorResponseDTO {

  private String status;
  private String message;

  public RegisterErrorResponseDTO(String message) {
    this.status = "error";
    this.message = message;
  }

  public RegisterErrorResponseDTO() {
    this.status = "error";
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}