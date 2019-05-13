package ru.bbpax.keeper.repo.note;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

@Repository
public interface NoteRepo extends AbstractNoteRepo<Note>, CustomNoteRepo {
    @Override
    default List<Note> findAll() {
        return findAllByNoteType(NOTE);
    }
}
