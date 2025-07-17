package com.demo.demo.service;

import com.demo.demo.entity.Notes.Note;
import com.demo.demo.repository.Note.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testCreateNote_Success() {
        // Arrange
        Note note = createTestNote(null, 1, "Test Title", "Test Description");
        Note savedNote = createTestNote(1, 1, "Test Title", "Test Description");
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        // Act
        Note result = noteService.createNote(note);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getPlanId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testCreateNote_WithAllFields() {
        // Arrange
        Note note = createTestNote(null, 2, "Important Note", "This is very important description");
        Note savedNote = createTestNote(5, 2, "Important Note", "This is very important description");
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        // Act
        Note result = noteService.createNote(note);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals(2, result.getPlanId());
        assertEquals("Important Note", result.getTitle());
        assertEquals("This is very important description", result.getDescription());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testCreateNote_EmptyDescription() {
        // Arrange
        Note note = createTestNote(null, 1, "Empty Note", "");
        Note savedNote = createTestNote(1, 1, "Empty Note", "");
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        // Act
        Note result = noteService.createNote(note);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getDescription());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testGetAllNotes_Success() {
        // Arrange
        List<Note> mockNotes = Arrays.asList(
                createTestNote(1, 1, "Note 1", "Description 1"),
                createTestNote(2, 2, "Note 2", "Description 2"),
                createTestNote(3, 1, "Note 3", "Description 3"));
        when(noteRepository.findAll()).thenReturn(mockNotes);

        // Act
        List<Note> result = noteService.getAllNotes();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Note 1", result.get(0).getTitle());
        assertEquals("Note 2", result.get(1).getTitle());
        assertEquals("Note 3", result.get(2).getTitle());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void testGetAllNotes_EmptyList() {
        // Arrange
        when(noteRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Note> result = noteService.getAllNotes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void testGetNoteById_Success() {
        // Arrange
        Integer noteId = 1;
        Note mockNote = createTestNote(noteId, 1, "Found Note", "Found Description");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(mockNote));

        // Act
        Note result = noteService.getNoteById(noteId);

        // Assert
        assertNotNull(result);
        assertEquals(noteId, result.getId());
        assertEquals("Found Note", result.getTitle());
        assertEquals("Found Description", result.getDescription());
        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetNoteById_NotFound() {
        // Arrange
        Integer noteId = 999;
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        // Act
        Note result = noteService.getNoteById(noteId);

        // Assert
        assertNull(result);
        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetNoteById_MultipleQueries() {
        // Arrange
        Note note1 = createTestNote(1, 1, "Note 1", "Description 1");
        Note note2 = createTestNote(2, 2, "Note 2", "Description 2");

        when(noteRepository.findById(1)).thenReturn(Optional.of(note1));
        when(noteRepository.findById(2)).thenReturn(Optional.of(note2));
        when(noteRepository.findById(3)).thenReturn(Optional.empty());

        // Act
        Note result1 = noteService.getNoteById(1);
        Note result2 = noteService.getNoteById(2);
        Note result3 = noteService.getNoteById(3);

        // Assert
        assertNotNull(result1);
        assertEquals("Note 1", result1.getTitle());

        assertNotNull(result2);
        assertEquals("Note 2", result2.getTitle());

        assertNull(result3);

        verify(noteRepository, times(1)).findById(1);
        verify(noteRepository, times(1)).findById(2);
        verify(noteRepository, times(1)).findById(3);
    }

    @Test
    void testDeleteNoteById_Success() {
        // Arrange
        Integer noteId = 1;
        doNothing().when(noteRepository).deleteById(noteId);

        // Act
        noteService.deleteNoteById(noteId);

        // Assert
        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    void testDeleteNoteById_MultipleDeletions() {
        // Arrange
        Integer[] noteIds = { 1, 2, 3, 4, 5 };
        for (Integer id : noteIds) {
            doNothing().when(noteRepository).deleteById(id);
        }

        // Act
        for (Integer id : noteIds) {
            noteService.deleteNoteById(id);
        }

        // Assert
        verify(noteRepository, times(5)).deleteById(any(Integer.class));
    }

    @Test
    void testDeleteNoteById_NonExistentNote() {
        // Arrange
        Integer nonExistentId = 999;
        doNothing().when(noteRepository).deleteById(nonExistentId);

        // Act
        noteService.deleteNoteById(nonExistentId);

        // Assert
        verify(noteRepository, times(1)).deleteById(nonExistentId);
    }

    // Test transaction behavior for createNote
    @Test
    void testCreateNote_TransactionalBehavior() {
        // Arrange
        Note note1 = createTestNote(null, 1, "First Note", "First Description");
        Note note2 = createTestNote(null, 1, "Second Note", "Second Description");

        Note savedNote1 = createTestNote(1, 1, "First Note", "First Description");
        Note savedNote2 = createTestNote(2, 1, "Second Note", "Second Description");

        when(noteRepository.save(note1)).thenReturn(savedNote1);
        when(noteRepository.save(note2)).thenReturn(savedNote2);

        // Act
        Note result1 = noteService.createNote(note1);
        Note result2 = noteService.createNote(note2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
        verify(noteRepository, times(2)).save(any(Note.class));
    }

    // Helper method to create test notes
    private Note createTestNote(Integer id, Integer planId, String title, String description) {
        Note note = new Note();
        note.setId(id);
        note.setPlanId(planId);
        note.setTitle(title);
        note.setDescription(description);
        note.setCreatedAt(OffsetDateTime.now());
        return note;
    }
}
