package ru.bbpax.keeper.repo.note;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.model.Tag;

import java.util.List;

import static ru.bbpax.keeper.model.QNote.note;
import static ru.bbpax.keeper.model.QTag.tag;

@Repository("noteRepoImpl")
public class CustomNoteRepoImpl extends QuerydslRepositorySupport implements CustomNoteRepo {
    public CustomNoteRepoImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public List<Note> findAllByTagId(String tagId) {
        return from(note)
                .where(note.tags.contains(findTag(tagId)))
                .fetch();
    }

    private Tag findTag(String tagId) {
        return from(tag).where(tag.id.eq(tagId)).fetchOne();
    }
}
