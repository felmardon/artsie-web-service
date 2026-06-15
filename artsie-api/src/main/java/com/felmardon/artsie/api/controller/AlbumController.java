package com.felmardon.artsie.api.controller;

import com.felmardon.artsie.api.dto.AlbumDto;
import com.felmardon.artsie.api.dto.PhotoDto;
import com.felmardon.artsie.api.mapper.GalleryMapper;
import com.felmardon.artsie.domain.repository.AlbumRepository;
import com.felmardon.artsie.domain.repository.PhotoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Public REST endpoints for browsing albums and their photos.
 * All endpoints are read-only and require no authentication.
 */
@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final GalleryMapper mapper;

    public AlbumController(AlbumRepository albumRepository,
                           PhotoRepository photoRepository,
                           GalleryMapper mapper) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.mapper = mapper;
    }

    /** Lists all published albums for the gallery landing page. */
    @GetMapping
    public List<AlbumDto> listAlbums() {
        return albumRepository.findByPublishedTrueOrderBySortOrderAsc()
                .stream()
                .map(mapper::toAlbumDto)
                .toList();
    }

    /** Returns a single album's metadata by ID. */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable UUID id) {
        return albumRepository.findById(id)
                .filter(a -> a.isPublished())
                .map(mapper::toAlbumDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Lists all published photos within an album. */
    @GetMapping("/{id}/photos")
    public List<PhotoDto> listAlbumPhotos(@PathVariable UUID id) {
        return photoRepository.findByAlbumIdAndPublishedTrueOrderBySortOrderAsc(id)
                .stream()
                .map(mapper::toPhotoDto)
                .toList();
    }
}
