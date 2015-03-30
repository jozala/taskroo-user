package com.taskroo.user.data;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import com.mongodb.DB;
import com.mongodb.MongoException;
import com.taskroo.mongo.MongoConnector;
import com.taskroo.user.domain.Role;
import com.taskroo.user.domain.User;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.runner.RunWith;

import java.util.Collections;

import static com.insightfullogic.lambdabehave.Suite.describe;

@RunWith(JunitSuiteRunner.class)
public class UserDaoIntegrationTest {{

    DB db = new MongoConnector(System.getProperties().getProperty("MONGO_PORT_27017_TCP_ADDR", "localhost"),
            System.getProperties().getProperty("MONGO_PORT_27017_TCP_PORT", "27017")).getDatabase("taskroo-dao-tests-db");
    Jongo jongo = new Jongo(db);
    MongoCollection usersCollection = jongo.getCollection("users");

    describe("userDao", it -> {

        UserDao userDao = new UserDao(usersCollection);

        it.isSetupWith(usersCollection::drop);

        it.should("save user in DB", expect -> {
            User user = new User("userName", "email@example.com", "fName", "sName", "pass", Collections.singleton(Role.USER),
                    false, "someSalt");
            userDao.createUser(user);
            expect.that(usersCollection.count("{_id: 'userName', firstName: 'fName'}")).isEqualTo(1L);
        });

        it.should("throw exception when trying to add user with existing username/_id", expect -> {
            User user1 = new User("userName", "user1@example.com", "fName1", "sName1", "pass1", Collections.singleton(Role.USER),
                    false, "someSalt");
            User user2 = new User("userName", "user2@example.com", "fName2", "sName2", "pass2", Collections.singleton(Role.USER),
                    false, "someSalt");
            userDao.createUser(user1);

            expect.exception(MongoException.DuplicateKey.class, () -> userDao.createUser(user2));
        });

        it.should("return user when new user has been created correctly in the DB", expect -> {
            User user = new User("userName", "email@example.com", "fName", "sName", "pass", Collections.singleton(Role.USER),
                    false, "someSalt");
            expect.that(userDao.createUser(user)).sameInstance(user);
        });

        it.should("return true when customer exists", expect -> {
            User user = new User("userName", "email@example.com", "fName", "sName", "pass", Collections.singleton(Role.USER),
                    false, "someSalt");
            userDao.createUser(user);
            expect.that(userDao.exists(user.get_id())).is(true);
        });

        it.should("throw exception when trying to save user without salt set", expect -> {
            User user = new User("userName", "email@example.com", "fName", "sName", "pass", Collections.singleton(Role.USER),
                    false, null);
            expect.exception(InvalidDataOperationException.class, () -> userDao.createUser(user));
        });
    });

}}