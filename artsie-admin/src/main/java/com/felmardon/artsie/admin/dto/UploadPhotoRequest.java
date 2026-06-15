package com.felmardon.artsie.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

/**
 * Metadata for a photo upload. Sent as the JSON part of a multipart request;
 * the actual file is sent as the multipart file part.
 */
public record UploadPhotoRequest(
    @NotBlank @Size(max = 150) String title,
    @Size(max = 500) String caption,
    UUID albumId,
    Set<String> tags,
    int sortOrder,
    boolean published
) {}
