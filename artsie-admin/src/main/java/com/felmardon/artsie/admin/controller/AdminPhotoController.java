package com.felmardon.artsie.admin.controller;

import com.felmardon.artsie.admin.dto.UploadPhotoRequest;
import com.felmardon.artsie.domain.entity.Album;
import com.felmardon.artsie.domain.entity.Photo;
import com.felmardon.artsie.domain.entity.Tag;
import com.felmardon.artsie.domain.repository.AlbumRepository;
import com.felmardon.artsie.domain.repository.PhotoRepository;
import com.felmardon.artsie.domain.repository.TagRepository;
import com.felmardon.artsie.storage.service.S3StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

/**
 * Admin CMS endpoints for uploading and managing photos.
 * Handles multipart file uploads, S3 storage, and metadata persistence.
 */
@RestController
@RequestMapping("/api/admin/photos")
public class AdminPhotoController {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final TagRepository tagRepository;
    private final S3StorageService storageService;

    public AdminPhotoController(PhotoRepository photoRepository,
                                AlbumRepository albumRepository,
                                TagRepository tagRepository,
                                S3StorageService storageService) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.tagRepository = tagRepository;
        this.storageService = storageService;
    }

    /**
     * Uploads a photo file and creates the corresponding database record.
     * Accepts a multipart request with the image file and JSON metadata.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Photo> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") UploadPhotoRequest metadata) throws IOException {

        // Validate file is an image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be an image");
        }

        // Upload to S3
        String s3Key = storageService.uploadPhoto(
                file.getOriginalFilename(),
                contentType,
                file.getInputStream(),
                file.getSize());

        // Build the entity
        Photo photo = new Photo();
        photo.setTitle(metadata.title());
        photo.setCaption(metadata.caption());
        photo.setS3Key(s3Key);
        photo.setContentType(contentType);
        photo.setFileSizeBytes(file.getSize());
        photo.setSortOrder(metadata.sortOrder());
        photo.setPublished(metadata.published());

        // Associate with album if specified
        if (metadata.albumId() != null) {
            Album album = albumRepository.findById(metadata.albumId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Album not found: " + metadata.albumId()));
            photo.setAlbum(album);
        }

        // Resolve or create tags
        if (metadata.tags() != null) {
            for (String tagName : metadata.tags()) {
                Tag tag = tagRepository.findByNameIgnoreCase(tagName.trim())
                        .orElseGet(() -> tagRepository.save(new Tag(tagName.trim().toLowerCase())));
                photo.addTag(tag);
            }
        }

        Photo saved = photoRepository.save(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Deletes a photo and its S3 objects. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));

        // Clean up S3
        storageService.delete(photo.getS3Key());
        if (photo.getThumbnailKey() != null) {
            storageService.delete(photo.getThumbnailKey());
        }

        photoRepository.delete(photo);
        return ResponseEntity.noContent().build();
    }

    /** Toggles the published status of a photo. */
    @PatchMapping("/{id}/publish")
    public Photo togglePublish(@PathVariable UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));

        photo.setPublished(!photo.isPublished());
        return photoRepository.save(photo);
    }
}
