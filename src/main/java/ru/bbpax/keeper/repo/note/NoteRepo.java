package ru.bbpax.keeper.repo.note;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

@Repository
public interface NoteRepo extends AbstractNoteRepo<Note>, CustomNoteRepo {
}
