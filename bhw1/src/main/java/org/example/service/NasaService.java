package org.example.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.nasa.NasaPhoto;
import org.example.model.nasa.NasaPhotos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class NasaService {

  private final RestTemplate restTemplate;

  @Value("${nasa.photos.url}")
  private String NASA_URL;
  @Value("${nasa.api-key}")
  private String NASA_API_KEY;

  public List<NasaPhoto> getPhotos(Integer sol) {
    URI nasaUri = UriComponentsBuilder.fromHttpUrl(NASA_URL)
        .queryParam("sol", sol)
        .queryParam("api_key", NASA_API_KEY)
        .build()
        .toUri();

    NasaPhotos nasaPhotos = restTemplate.getForObject(nasaUri, NasaPhotos.class);

    if (nasaPhotos == null) {
      return new ArrayList<>();
    }

    return nasaPhotos.getNasaPhotoList();
  }

}
