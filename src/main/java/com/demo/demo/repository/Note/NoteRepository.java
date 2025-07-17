package com.demo.demo.repository.Note;

import com.demo.demo.entity.Notes.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  NoteRepository extends JpaRepository<Note, Integer> {

}
