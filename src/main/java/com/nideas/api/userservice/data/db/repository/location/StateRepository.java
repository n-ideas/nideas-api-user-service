package com.nideas.api.userservice.data.db.repository.location;

import com.nideas.api.userservice.data.db.entity.location.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Created by Nanugonda on 7/10/2018. */
@Repository
public interface StateRepository extends CrudRepository<State, Long> {}
