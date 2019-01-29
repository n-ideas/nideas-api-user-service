package com.nideas.api.userservice.data.db.entity.location;

import com.nideas.api.userservice.data.db.entity.AuditEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/** Created by Nanugonda on 6/20/2018. */
@Data
@Entity
@Table(name = "country")
public class Country extends AuditEntity {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToMany(
      mappedBy = "country",
      targetEntity = State.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY)
  private List<State> states;
  // Getters and Setters are generated by the class level annotation lombok.Data
}