package org.example.repository;

import static org.example.generated.jooq.Tables.CAMERAS;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.model.Camera;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CamerasRepository {

  private final DSLContext jooq;

  public Optional<Integer> getCameraId(Integer nasaId) {
    try {
      return Optional.of(
          jooq
              .select(CAMERAS.ID)
              .from(CAMERAS)
              .where(CAMERAS.NASA_ID.eq(nasaId))
              .fetchSingle()
              .into(Integer.class));
    } catch (NoDataFoundException e) {
      return Optional.empty();
    }
  }

  public Integer createCamera(Camera camera) {
    return jooq.insertInto(CAMERAS)
        .set(CAMERAS.NASA_ID, camera.getNasaId())
        .set(CAMERAS.NAME, camera.getName())
        .onDuplicateKeyIgnore()
        .returningResult(CAMERAS.ID)
        .fetchSingle()
        .into(Integer.class);
  }

}
