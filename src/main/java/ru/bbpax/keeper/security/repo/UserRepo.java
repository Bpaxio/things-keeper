package ru.bbpax.keeper.security.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.security.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends MongoRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
