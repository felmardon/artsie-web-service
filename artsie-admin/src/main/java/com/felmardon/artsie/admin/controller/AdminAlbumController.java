package com.felmardon.artsie.admin.controller;

import com.felmardon.artsie.admin.dto.CreateAlbumRequest;
import com.felmardon.artsie.domain.entity.Album;
import com.felmardon.artsie.domain.repository.AlbumRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Admin CMS endpoints for managing albums.
 * All endpoints require authentication (enforced by SecurityConfig).
 */
@RestController
@RequestMapping("/api/admin/albums")
public class AdminAlbumController {

    private final AlbumRepository albumRepository;

    public AdminAlbumController(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /** Lists all albums (including unpublished) for the admin dashboard. */
    @GetMapping
    public List<Album> listAll() {
        return albumRepository.findAllByOrderBySortOrderAsc();
    }

    /** Creates a new album. */
    @PostMapping
    public ResponseEntity<Album> create(@Valid @RequestBody CreateAlbumRequest request) {
        Album album = new Album(request.title());
        album.setDescription(request.description());
        album.setSortOrder(request.sortOrder());
        album.setPublished(request.published());

        Album saved = albumRepository.save(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Updates an existing album's metadata. */
    @PutMapping("/{id}")
    public Album update(@PathVariable UUID id, @Valid @RequestBody CreateAlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));

        album.setTitle(request.title());
        album.setDescription(request.description());
        album.setSortOrder(request.sortOrder());
        album.setPublished(request.published());

        return albumRepository.save(album);
    }

    /** Deletes an album and all its photos (cascaded). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!albumRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
