package ru.bbpax.keeper.repo.linkmark;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;
import java.util.Optional;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;

@Repository
public interface LinkMarkRepo extends AbstractNoteRepo<LinkMark> {
    @Override
    @NonNull
    default Optional<LinkMark> findById(@NonNull String id) {
        return findByIdAndNoteType(id, LINK_MARK);
    }

    @Override
    default void deleteById(@NonNull String id) {
        deleteById(id, LINK_MARK);
    }

    @Override
    @NonNull
    default List<LinkMark> findAll() {
        return findAllByNoteType(LINK_MARK);
    }
}
