package ru.bbpax.keeper.repo;

import ru.bbpax.keeper.model.AbstractNote;

import java.util.List;

public interface CustomNoteRepo {
    List<? extends AbstractNote> findAllByTagId(String tagId);
}
