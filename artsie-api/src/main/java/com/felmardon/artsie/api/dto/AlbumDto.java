package com.felmardon.artsie.api.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Read-only DTO for album listings in the public gallery.
 * Does not include the full photo list — use the photo endpoint
 * with an album ID to fetch photos within an album.
 */
public record AlbumDto(
    UUID id,
    String title,
    String description,
    String coverImageUrl,
    int photoCount,
    int sortOrder,
    Instant createdAt
) {}
