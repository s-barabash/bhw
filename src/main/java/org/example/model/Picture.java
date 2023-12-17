package org.example.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Picture {

  private Integer nasaId;
  private String imageSrc;
}
