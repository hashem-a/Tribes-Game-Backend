package com.gattoverdetribes.gattoverdetribes.repositories;

import com.gattoverdetribes.gattoverdetribes.models.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
  ConfirmationToken findByToken(String confirmationToken);
}