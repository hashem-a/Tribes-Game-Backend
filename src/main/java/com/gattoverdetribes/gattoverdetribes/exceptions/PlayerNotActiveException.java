package com.gattoverdetribes.gattoverdetribes.exceptions;

public class PlayerNotActiveException extends RuntimeException {

  public PlayerNotActiveException(String errorMessage) {
    super(errorMessage);
  }
}