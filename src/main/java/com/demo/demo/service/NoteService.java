package com.demo.demo.service;

import com.demo.demo.entity.Notes.Note;
import com.demo.demo.repository.Note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Transactional
    public Note createNote(Note note){
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Integer id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void deleteNoteById(Integer id) {
        noteRepository.deleteById(id);
    }
}