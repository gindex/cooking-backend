package com.github.cookingbackend.repositories;

import com.github.cookingbackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TagRepository extends JpaRepository<Tag, Long> {
}
