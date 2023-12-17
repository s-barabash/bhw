package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.NasaCrawlerReq;
import org.example.service.NasaStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NasaPicturesController {

  private final NasaStoreService nasaStoreService;

  @PostMapping("/pictures/steal")
  @ResponseStatus(HttpStatus.CREATED)
  public void getNasaPictures(@RequestBody NasaCrawlerReq req) {
    nasaStoreService.storeNasaPhotos(req.sol());
  }

}
