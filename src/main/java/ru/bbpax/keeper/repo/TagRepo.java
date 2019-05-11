package ru.bbpax.keeper.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Tag;

@Repository
public interface TagRepo extends MongoRepository<Tag, String>, CustomTagRepo {
}
