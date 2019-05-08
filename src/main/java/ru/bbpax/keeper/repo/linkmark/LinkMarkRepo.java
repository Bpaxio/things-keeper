package ru.bbpax.keeper.repo.linkmark;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

@Repository
public interface LinkMarkRepo extends AbstractNoteRepo<LinkMark> {
}
