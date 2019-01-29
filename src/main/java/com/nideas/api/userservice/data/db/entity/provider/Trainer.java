package com.nideas.api.userservice.data.db.entity.provider;

import com.nideas.api.userservice.data.db.entity.AuditEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/** Created by Nanugonda on 6/20/2018. */
@Data
@Entity
@Table(name = "trainer")
public class Trainer extends AuditEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  private String name;

  // Getters and Setters are generated by the class level annotation lombok.Data
}
