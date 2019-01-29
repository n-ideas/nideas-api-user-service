package com.nideas.api.userservice.data.db.entity.file;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/** Created by Nanugonda on 9/10/2018. */
@Data
@Entity
@Table(name = "profilePicture")
public class ProfilePicture {

  @Id private long id;
  private byte[] file;
  private String name;
  private String type;
}
