package com.felmardon.artsie.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for AWS S3 integration.
 * Bound from the {@code artsie.storage.s3} prefix in application.yml.
 */
@ConfigurationProperties(prefix = "artsie.storage.s3")
public class S3Properties {

    /** Name of the S3 bucket used for media storage. */
    private String bucketName;

    /** AWS region where the bucket lives (e.g., "us-east-1"). */
    private String region = "us-east-1";

    /** Prefix applied to all photo object keys. */
    private String photoPrefix = "photos/";

    /** Prefix applied to all video object keys. */
    private String videoPrefix = "videos/";

    /** Prefix for generated thumbnails. */
    private String thumbnailPrefix = "thumbnails/";

    /** Duration in minutes for presigned URL validity. */
    private int presignedUrlExpirationMinutes = 60;

    // ---- Getters and Setters ----

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhotoPrefix() {
        return photoPrefix;
    }

    public void setPhotoPrefix(String photoPrefix) {
        this.photoPrefix = photoPrefix;
    }

    public String getVideoPrefix() {
        return videoPrefix;
    }

    public void setVideoPrefix(String videoPrefix) {
        this.videoPrefix = videoPrefix;
    }

    public String getThumbnailPrefix() {
        return thumbnailPrefix;
    }

    public void setThumbnailPrefix(String thumbnailPrefix) {
        this.thumbnailPrefix = thumbnailPrefix;
    }

    public int getPresignedUrlExpirationMinutes() {
        return presignedUrlExpirationMinutes;
    }

    public void setPresignedUrlExpirationMinutes(int presignedUrlExpirationMinutes) {
        this.presignedUrlExpirationMinutes = presignedUrlExpirationMinutes;
    }
}
