package ru.bbpax.keeper.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.security.SecurityUtil;
import ru.bbpax.keeper.security.model.User;
import ru.bbpax.keeper.security.repo.UserRepo;

@Slf4j
@Service
@AllArgsConstructor
public class PrivilegeService {
    private final UserRepo userRepo;

    public void addPrivilege(String targetId, String accessLevel) {
        User user = SecurityUtil.getWithPrivilege(targetId, accessLevel);
        log.info("save user: {}", user);
        userRepo.save(user);
    }
}
