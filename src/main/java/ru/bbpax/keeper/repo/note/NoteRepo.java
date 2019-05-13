package ru.bbpax.keeper.repo.note;

import com.querydsl.core.types.Predicate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;
import java.util.Optional;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

@Repository
public interface NoteRepo extends AbstractNoteRepo<Note> {

    @Override
    @NonNull
    default Optional<Note> findById(@NonNull String id) {
        return findByIdAndNoteType(id, NOTE);
    }

    @Override
    default void deleteById(@NonNull String id) {
        deleteById(id, NOTE);
    }

    @Override
    @NonNull
    default List<Note> findAll() {
        return findAllByNoteType(NOTE);
    }

    @Override
    @NonNull
    List<Note> findAll(@NonNull Predicate predicate);
}
