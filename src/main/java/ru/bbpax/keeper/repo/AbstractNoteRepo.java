package ru.bbpax.keeper.repo;

import com.querydsl.core.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import ru.bbpax.keeper.filter.core.Filter;
import ru.bbpax.keeper.model.AbstractNote;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractNoteRepo<T extends AbstractNote>
        extends MongoRepository<T, String>, QuerydslPredicateExecutor<T> {

    List<T> findAllByNoteType(@NonNull String noteType);

    Optional<T> findByIdAndNoteType(@NonNull String id, @NonNull String noteType);

    void deleteById(@NonNull String id, @NonNull String noteType);

    default List<T> findAll(@NonNull Filter filter) {
        return findAll(filter.toPredicate());
    }

    @Override
    @NonNull
    List<T> findAll(@NonNull Predicate predicate);
}
