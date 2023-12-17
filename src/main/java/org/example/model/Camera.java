package org.example.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Camera {

  private Integer nasaId;
  private String name;
  private List<Picture> pictures;
}
