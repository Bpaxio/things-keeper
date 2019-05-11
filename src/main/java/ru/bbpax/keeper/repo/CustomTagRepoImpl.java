package ru.bbpax.keeper.repo;


import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.bbpax.keeper.model.Tag;

import static ru.bbpax.keeper.model.QNote.note;

@Repository("tagRepoImpl")
public class CustomTagRepoImpl extends QuerydslRepositorySupport implements CustomTagRepo {
    public CustomTagRepoImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public boolean tagIsUsed(Tag tag) {
        Assert.notNull(tag, "The given tag must not be null!");
        return !from(note).where(note.tags.contains(tag)).fetch().isEmpty();
    }
}
