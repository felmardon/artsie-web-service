package com.felmardon.artsie.domain.repository;

import com.felmardon.artsie.domain.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    /** All published photos in an album, ordered for gallery display. */
    List<Photo> findByAlbumIdAndPublishedTrueOrderBySortOrderAsc(UUID albumId);

    /** All photos in an album (published or not) for admin views. */
    List<Photo> findByAlbumIdOrderBySortOrderAsc(UUID albumId);

    /** Published photos that carry a specific tag. */
    @Query("SELECT p FROM Photo p JOIN p.tags t WHERE t.name = :tagName AND p.published = true ORDER BY p.sortOrder")
    List<Photo> findPublishedByTagName(@Param("tagName") String tagName);

    /** All published photos not assigned to any album. */
    List<Photo> findByAlbumIsNullAndPublishedTrueOrderBySortOrderAsc();
}
