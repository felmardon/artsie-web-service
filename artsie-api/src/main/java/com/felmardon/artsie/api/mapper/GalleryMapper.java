package com.felmardon.artsie.api.mapper;

import com.felmardon.artsie.api.dto.AlbumDto;
import com.felmardon.artsie.api.dto.PhotoDto;
import com.felmardon.artsie.domain.entity.Album;
import com.felmardon.artsie.domain.entity.Photo;
import com.felmardon.artsie.domain.entity.Tag;
import com.felmardon.artsie.storage.service.S3StorageService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Maps domain entities to public-facing DTOs. Resolves S3 object keys
 * to presigned URLs so the frontend never handles raw keys.
 */
@Component
public class GalleryMapper {

    private final S3StorageService storageService;

    public GalleryMapper(S3StorageService storageService) {
        this.storageService = storageService;
    }

    public PhotoDto toPhotoDto(Photo photo) {
        return new PhotoDto(
            photo.getId(),
            photo.getTitle(),
            photo.getCaption(),
            resolveUrl(photo.getS3Key()),
            resolveUrl(photo.getThumbnailKey()),
            photo.getContentType(),
            photo.getWidth(),
            photo.getHeight(),
            photo.getCameraMake(),
            photo.getCameraModel(),
            photo.getLens(),
            photo.getFocalLength(),
            photo.getAperture(),
            photo.getShutterSpeed(),
            photo.getIso(),
            photo.getTags().stream().map(Tag::getName).collect(Collectors.toSet()),
            photo.getSortOrder(),
            photo.getCreatedAt()
        );
    }

    public AlbumDto toAlbumDto(Album album) {
        String coverUrl = null;
        if (album.getCoverImageKey() != null) {
            coverUrl = resolveUrl(album.getCoverImageKey());
        } else if (!album.getPhotos().isEmpty()) {
            // Fall back to the first photo's thumbnail as the cover
            Photo firstPhoto = album.getPhotos().get(0);
            String key = firstPhoto.getThumbnailKey() != null
                    ? firstPhoto.getThumbnailKey()
                    : firstPhoto.getS3Key();
            coverUrl = resolveUrl(key);
        }

        long publishedCount = album.getPhotos().stream()
                .filter(Photo::isPublished)
                .count();

        return new AlbumDto(
            album.getId(),
            album.getTitle(),
            album.getDescription(),
            coverUrl,
            (int) publishedCount,
            album.getSortOrder(),
            album.getCreatedAt()
        );
    }

    private String resolveUrl(String s3Key) {
        if (s3Key == null || s3Key.isBlank()) {
            return null;
        }
        return storageService.generatePresignedGetUrl(s3Key);
    }
}
