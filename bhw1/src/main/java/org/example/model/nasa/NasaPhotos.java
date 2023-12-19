package org.example.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class NasaPhotos {

  @JsonProperty("photos")
  private List<NasaPhoto> nasaPhotoList;
}
