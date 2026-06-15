package com.felmardon.artsie.api.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Read-only DTO returned by the public gallery API.
 * Contains the presigned image URL so the frontend never sees raw S3 keys.
 */
public record PhotoDto(
    UUID id,
    String title,
    String caption,
    String imageUrl,
    String thumbnailUrl,
    String contentType,
    Integer width,
    Integer height,
    String cameraMake,
    String cameraModel,
    String lens,
    String focalLength,
    String aperture,
    String shutterSpeed,
    Integer iso,
    Set<String> tags,
    int sortOrder,
    Instant createdAt
) {}
