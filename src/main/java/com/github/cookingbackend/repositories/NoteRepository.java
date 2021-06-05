package com.github.cookingbackend.repositories;

import com.github.cookingbackend.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NoteRepository extends JpaRepository<Note, Long> {
}
