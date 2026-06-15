package com.felmardon.artsie.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single photograph in the portfolio.
 * <p>
 * The actual image file lives in S3; this entity stores the S3 object key,
 * a generated thumbnail key, display metadata (title, caption), and optional
 * EXIF-derived fields (camera, lens, ISO, etc.) that can be shown in the gallery.
 */
@Entity
@Table(name = "photos")
public class Photo extends BaseEntity {

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @Size(max = 500)
    @Column(length = 500)
    private String caption;

    // ---- S3 references ----

    /** S3 object key for the full-resolution image (e.g., "photos/uuid/original.jpg"). */
    @NotBlank
    @Size(max = 512)
    @Column(nullable = false, length = 512)
    private String s3Key;

    /** S3 object key for the generated thumbnail. */
    @Size(max = 512)
    @Column(length = 512)
    private String thumbnailKey;

    /** MIME type of the uploaded file (e.g., "image/jpeg", "image/png"). */
    @Size(max = 50)
    @Column(length = 50)
    private String contentType;

    /** Original file size in bytes. */
    private Long fileSizeBytes;

    /** Image width in pixels. */
    private Integer width;

    /** Image height in pixels. */
    private Integer height;

    // ---- EXIF metadata (optional, extracted on upload) ----

    @Size(max = 100)
    @Column(length = 100)
    private String cameraMake;

    @Size(max = 100)
    @Column(length = 100)
    private String cameraModel;

    @Size(max = 100)
    @Column(length = 100)
    private String lens;

    @Size(max = 20)
    @Column(length = 20)
    private String focalLength;

    @Size(max = 20)
    @Column(length = 20)
    private String aperture;

    @Size(max = 20)
    @Column(length = 20)
    private String shutterSpeed;

    private Integer iso;

    // ---- Relationships ----

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany
    @JoinTable(
        name = "photo_tags",
        joinColumns = @JoinColumn(name = "photo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /** Display order within the album. Lower values appear first. */
    @Column(nullable = false)
    private int sortOrder = 0;

    @Column(nullable = false)
    private boolean published = false;

    public Photo() {}

    // ---- Convenience methods ----

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPhotos().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPhotos().remove(this);
    }

    // ---- Getters and Setters ----

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getThumbnailKey() {
        return thumbnailKey;
    }

    public void setThumbnailKey(String thumbnailKey) {
        this.thumbnailKey = thumbnailKey;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public void setCameraMake(String cameraMake) {
        this.cameraMake = cameraMake;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public Integer getIso() {
        return iso;
    }

    public void setIso(Integer iso) {
        this.iso = iso;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
}
