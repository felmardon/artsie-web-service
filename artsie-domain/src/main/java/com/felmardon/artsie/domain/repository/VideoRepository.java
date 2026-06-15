package com.felmardon.artsie.domain.repository;

import com.felmardon.artsie.domain.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    List<Video> findByAlbumIdAndPublishedTrueOrderBySortOrderAsc(UUID albumId);
}
