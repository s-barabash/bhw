package org.example.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NasaPhoto {

  private Integer id;
  @JsonProperty("img_src")
  private String imgSrc;
  private NasaCamera camera;
}
