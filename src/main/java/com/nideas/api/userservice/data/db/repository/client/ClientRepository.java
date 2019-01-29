package com.nideas.api.userservice.data.db.repository.client;

import com.nideas.api.userservice.data.db.entity.client.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Created by Nanugonda on 7/11/2018. */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  boolean existsByEmail(String email);

  Optional<Client> findByEmail(String email);

  Optional<Client> findByToken(String token);
}
