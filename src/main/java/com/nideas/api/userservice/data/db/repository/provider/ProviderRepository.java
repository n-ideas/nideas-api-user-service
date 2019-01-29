package com.nideas.api.userservice.data.db.repository.provider;

import com.nideas.api.userservice.data.db.entity.provider.Provider;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Created by Nanugonda on 6/19/2018. */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

  Optional<Provider> findByEmail(String email);

  Optional<Provider> findByToken(String token);

  List<Provider> findAllByZipCode(Integer zipCode);

  List<Provider> findAllByCity(String city);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
