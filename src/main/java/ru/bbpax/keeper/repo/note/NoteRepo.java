package ru.bbpax.keeper.repo.note;

import com.querydsl.core.types.Predicate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

@Repository
public interface NoteRepo extends AbstractNoteRepo<Note> {
    @Override
    default List<Note> findAll() {
        return findAllByNoteType(NOTE);
    }

    @Override
    List<Note> findAll(@NonNull Predicate predicate);
}
