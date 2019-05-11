package ru.bbpax.keeper.repo;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Tag;

@Repository
public interface CustomTagRepo {
    boolean tagIsUsed(Tag tag);
}
