package com.nideas.api.userservice.data.db.entity.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/** Created by Nanugonda on 6/20/2018. */
@JsonFormat(shape = Shape.OBJECT)
public enum Gender {
  Female,
  Male,
  Other
}
