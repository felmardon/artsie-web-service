package com.felmardon.artsie.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating or updating an album via the admin CMS.
 */
public record CreateAlbumRequest(
    @NotBlank @Size(max = 100) String title,
    @Size(max = 500) String description,
    int sortOrder,
    boolean published
) {}
