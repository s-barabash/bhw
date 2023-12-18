package org.example.model;

import java.util.List;

public record Camera(Integer nasaId, String name, List<Picture> pictures) {

}
