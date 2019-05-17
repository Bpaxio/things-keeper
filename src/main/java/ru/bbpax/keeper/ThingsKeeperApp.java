package ru.bbpax.keeper;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.Converters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.bbpax.keeper.security.model.Privilege;
import ru.bbpax.keeper.security.model.User;
import ru.bbpax.keeper.security.repo.UserRepo;

import java.util.Collections;

import static ru.bbpax.keeper.security.model.AccessLevels.ALL;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories
public class ThingsKeeperApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ThingsKeeperApp.class);
//        UserRepo userRepo = run.getBean(UserRepo.class);
//        User bpaxio = new User();
//        bpaxio.setUsername("bpaxio");
//        bpaxio.setPassword("$2a$09$K8GVNxxWUxQON2V.NlCsdOx3bfKduRiC5.rbTnboRWTIoy65V6EtC");
////        bpaxio.setPassword("$2a$15$ERzUi87p3HSIEqvcDC23guIB8nEIUCL.X9iKz6iZLN6m.u/ZyQ06G");
//        bpaxio.setPrivileges(Collections
//                .singleton(new Privilege("5cdab05f94458d58e80e5378", ALL)));
//
//        User user = new User();
//        user.setUsername("user");
//        user.setPassword("$2a$09$IBbZL0E5McuwaID5d8iNdeb22GvZOvosiX1TqG52UoPcc0i2gqX");
////        user.setPassword("$2a$15$ZOwnUDjtgMpCRgjjHOtakus47pKPRoxZ2iZjOGgxdy0FJ9oOYZ69i");
//        userRepo.save(bpaxio);
//        userRepo.save(user);
    }
}
