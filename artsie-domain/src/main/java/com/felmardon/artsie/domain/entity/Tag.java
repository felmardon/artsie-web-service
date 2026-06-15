package com.felmardon.artsie.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * A label used to categorize photos (and eventually videos).
 * Examples: "nature", "portrait", "urban", "black-and-white".
 * Tags have a many-to-many relationship with Photo.
 */
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tag extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @ManyToMany(mappedBy = "tags")
    private Set<Photo> photos = new HashSet<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // ---- Getters and Setters ----

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
}
