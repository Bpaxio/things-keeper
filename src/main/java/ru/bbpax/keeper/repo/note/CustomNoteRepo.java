package ru.bbpax.keeper.repo.note;

import ru.bbpax.keeper.model.Note;

import java.util.List;

public interface CustomNoteRepo {
    List<Note> findAllByTagId(String tagId);
}
