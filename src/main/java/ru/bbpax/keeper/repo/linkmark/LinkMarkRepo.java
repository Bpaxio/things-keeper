package ru.bbpax.keeper.repo.linkmark;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;

@Repository
public interface LinkMarkRepo extends AbstractNoteRepo<LinkMark> {
    @Override
    default List<LinkMark> findAll() {
        return findAllByNoteType(LINK_MARK);
    }
}
