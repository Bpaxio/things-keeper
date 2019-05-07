package ru.bbpax.keeper.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.AbstractNote;

import java.util.List;

@Repository
public interface NoteRepo extends MongoRepository<AbstractNote, String>, QuerydslPredicateExecutor<AbstractNote>, CustomNoteRepo {

    <T extends AbstractNote> List<T> findAllByNoteType(String noteType);

}
