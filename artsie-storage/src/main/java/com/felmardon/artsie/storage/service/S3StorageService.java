package com.felmardon.artsie.storage.service;

import com.felmardon.artsie.storage.config.S3Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

/**
 * Handles all direct interactions with the S3 media bucket:
 * uploading files, deleting objects, and generating presigned URLs
 * for both uploads (admin) and downloads (gallery).
 */
@Service
public class S3StorageService {

    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties properties;

    public S3StorageService(S3Client s3Client, S3Presigner s3Presigner, S3Properties properties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
    }

    /**
     * Uploads a file to S3 under the given prefix and returns the full S3 object key.
     * A UUID is prepended to the filename to avoid collisions.
     *
     * @param prefix      the key prefix (e.g., "photos/" or "thumbnails/")
     * @param filename    the original filename
     * @param contentType MIME type of the file
     * @param inputStream the file content
     * @param contentLength size in bytes
     * @return the S3 object key where the file was stored
     */
    public String upload(String prefix, String filename, String contentType,
                         InputStream inputStream, long contentLength) {

        String key = prefix + UUID.randomUUID() + "/" + sanitizeFilename(filename);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength));
        log.info("Uploaded object to S3: {}", key);
        return key;
    }

    /**
     * Uploads a photo and returns the S3 key.
     */
    public String uploadPhoto(String filename, String contentType,
                              InputStream inputStream, long contentLength) {
        return upload(properties.getPhotoPrefix(), filename, contentType, inputStream, contentLength);
    }

    /**
     * Uploads a thumbnail and returns the S3 key.
     */
    public String uploadThumbnail(String filename, String contentType,
                                  InputStream inputStream, long contentLength) {
        return upload(properties.getThumbnailPrefix(), filename, contentType, inputStream, contentLength);
    }

    /**
     * Deletes an object from S3 by key.
     */
    public void delete(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
        log.info("Deleted object from S3: {}", key);
    }

    /**
     * Generates a presigned GET URL for downloading/viewing a media file.
     *
     * @param key the S3 object key
     * @return a presigned URL valid for the configured expiration duration
     */
    public String generatePresignedGetUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(properties.getPresignedUrlExpirationMinutes()))
                .getObjectRequest(builder -> builder
                        .bucket(properties.getBucketName())
                        .key(key))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    /**
     * Generates a presigned PUT URL for direct browser uploads from the admin CMS.
     *
     * @param key         the intended S3 object key
     * @param contentType the expected MIME type of the upload
     * @return a presigned URL the client can PUT to directly
     */
    public String generatePresignedPutUrl(String key, String contentType) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(properties.getPresignedUrlExpirationMinutes()))
                .putObjectRequest(builder -> builder
                        .bucket(properties.getBucketName())
                        .key(key)
                        .contentType(contentType))
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    /**
     * Strips path separators and special characters from filenames to prevent
     * S3 key injection or unexpected directory traversal.
     */
    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "unnamed";
        }
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
