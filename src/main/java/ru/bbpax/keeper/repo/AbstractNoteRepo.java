package ru.bbpax.keeper.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.bbpax.keeper.model.AbstractNote;

import java.util.List;

@NoRepositoryBean
public interface AbstractNoteRepo<T extends AbstractNote>
        extends MongoRepository<T, String>, QuerydslPredicateExecutor<T> {

    List<T> findAllByNoteType(String noteType);
}
