package com.felmardon.artsie.domain.repository;

import com.felmardon.artsie.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {

    /** Published albums for the public gallery, ordered for display. */
    List<Album> findByPublishedTrueOrderBySortOrderAsc();

    /** All albums for admin management. */
    List<Album> findAllByOrderBySortOrderAsc();
}
