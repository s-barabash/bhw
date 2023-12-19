package org.example.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.example.model.Camera;
import org.example.model.Picture;
import org.example.model.nasa.NasaPhoto;
import org.example.repository.CamerasRepository;
import org.example.repository.PicturesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NasaStoreService {

  private final static Logger log = LoggerFactory.getLogger(NasaStoreService.class);

  private final NasaService nasaService;
  private final CamerasRepository camerasRepository;
  private final PicturesRepository picturesRepository;

  @Transactional
  public void storeNasaPhotos(Integer sol) {
    log.info("Start to retrieve photos for sol:[{}]", sol);
    List<NasaPhoto> nasaPhotoList = nasaService.getPhotos(sol);
    log.info("Retrieved:[{}] photos", nasaPhotoList.size());

    if (nasaPhotoList.isEmpty()) {
      return;
    }

    Map<Camera, List<Picture>> picturesPerCameraMap = getPicturesPerCameraMap(nasaPhotoList);

    storePicturePerCamera(picturesPerCameraMap);
  }

  private static Map<Camera, List<Picture>> getPicturesPerCameraMap(List<NasaPhoto> nasaPhotoList) {
    return nasaPhotoList.stream()
        .collect(Collectors.toMap(
            nasaPhoto -> new Camera(nasaPhoto.getCamera().getId(),
                nasaPhoto.getCamera().getName(),
                new ArrayList<>()),
            nasaPhoto -> Collections.singletonList(new Picture(nasaPhoto.getId(),
                nasaPhoto.getImgSrc())),
            ListUtils::union
        ));
  }

  private void storePicturePerCamera(Map<Camera, List<Picture>> picturesPerCameraMap) {
    picturesPerCameraMap.forEach((key, value) -> {
      Integer cameraId = camerasRepository.getCameraIdByNasaId(key.nasaId())
          .orElseGet(() -> {
            log.info("No camera was found for cameraNasaId:[{}], creating new camera",
                key.nasaId());
            return camerasRepository.createCamera(key);
          });
      log.info("Creating pictures for camera with id:[{}]", cameraId);
      picturesRepository.createPicturesPerCamera(cameraId, value);
    });
  }

}
