package com.felmardon.artsie.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * A named collection of photos displayed as a gallery group.
 * Examples: "Mountain Landscapes", "Street Portraits", "Summer 2025".
 * Photos are ordered within an album by their sortOrder field.
 */
@Entity
@Table(name = "albums")
public class Album extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    /**
     * Optional S3 key for a cover image. If null, the first photo in the
     * album is used as the cover on the frontend.
     */
    @Size(max = 512)
    @Column(length = 512)
    private String coverImageKey;

    /**
     * Controls the display order of albums in the gallery.
     * Lower values appear first.
     */
    @Column(nullable = false)
    private int sortOrder = 0;

    @Column(nullable = false)
    private boolean published = false;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<Photo> photos = new ArrayList<>();

    public Album() {}

    public Album(String title) {
        this.title = title;
    }

    // ---- Convenience methods ----

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setAlbum(this);
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        photo.setAlbum(null);
    }

    // ---- Getters and Setters ----

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageKey() {
        return coverImageKey;
    }

    public void setCoverImageKey(String coverImageKey) {
        this.coverImageKey = coverImageKey;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
