package com.felmardon.artsie.api.controller;

import com.felmardon.artsie.api.dto.PhotoDto;
import com.felmardon.artsie.api.mapper.GalleryMapper;
import com.felmardon.artsie.domain.repository.PhotoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Public REST endpoints for browsing individual photos.
 * Supports fetching by ID and filtering by tag name.
 */
@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final GalleryMapper mapper;

    public PhotoController(PhotoRepository photoRepository, GalleryMapper mapper) {
        this.photoRepository = photoRepository;
        this.mapper = mapper;
    }

    /** Returns a single published photo by ID. */
    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhoto(@PathVariable UUID id) {
        return photoRepository.findById(id)
                .filter(p -> p.isPublished())
                .map(mapper::toPhotoDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Lists all published photos that carry the given tag. */
    @GetMapping(params = "tag")
    public List<PhotoDto> listByTag(@RequestParam String tag) {
        return photoRepository.findPublishedByTagName(tag)
                .stream()
                .map(mapper::toPhotoDto)
                .toList();
    }
}
