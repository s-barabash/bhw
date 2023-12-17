package org.example.repository;

import static org.example.generated.jooq.Tables.PICTURES;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.generated.jooq.tables.records.PicturesRecord;
import org.example.model.Picture;
import org.example.service.NasaStoreService;
import org.jooq.DSLContext;
import org.jooq.InsertReturningStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PicturesRepository {

  private final static Logger log = LoggerFactory.getLogger(NasaStoreService.class);

  private final DSLContext jooq;

  public int[] createPicturesPerCamera(Integer cameraId, List<Picture> pictures) {
    log.debug("Preparing statements for batch insert for camaraId:[{}], and pictures:[{}]",
        cameraId, pictures);

    List<InsertReturningStep<PicturesRecord>> insertList = pictures.stream()
        .map(picture -> jooq.insertInto(PICTURES)
            .set(PICTURES.CAMERA_ID, cameraId)
            .set(PICTURES.NASA_ID, picture.getNasaId())
            .set(PICTURES.IMAGE_SRC, picture.getImageSrc())
            .onDuplicateKeyIgnore())
        .toList();

    log.debug("Prepared statements:[{}]", insertList);

    int[] result = jooq.batch(insertList).execute();

    log.info("Batch insert was successfully executed with next result:[{}]", result);

    return result;
  }

}
