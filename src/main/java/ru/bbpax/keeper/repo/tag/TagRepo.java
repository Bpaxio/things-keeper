package ru.bbpax.keeper.repo.tag;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Tag;

import java.util.Optional;

@Repository
public interface TagRepo extends MongoRepository<Tag, String>, CustomTagRepo {
    Optional<Tag> findFirstByValue(String value);
}
