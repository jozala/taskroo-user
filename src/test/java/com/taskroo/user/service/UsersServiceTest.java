package com.taskroo.user.service;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import com.taskroo.user.data.UserDao;
import com.taskroo.user.domain.User;
import com.taskroo.user.domain.factory.SafeUserFactory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static com.insightfullogic.lambdabehave.Suite.describe;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.verify;

@RunWith(JunitSuiteRunner.class)
public class UsersServiceTest {{

    describe("UsersService", it -> {

        UserDao usersDaoMock = it.usesMock(UserDao.class);
        SafeUserFactory safeUserFactory = new SafeUserFactory();
        UsersService usersService = new UsersService(usersDaoMock, safeUserFactory);

        it.should("create new user in DB using auto-generated salt to hash password", expect -> {
            User user = User.createNewUser("userName", "email@taskroo.com", "fName", "sName", "unencryptedPassword");
            usersService.createUser(user);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(usersDaoMock).createUser(userCaptor.capture());

            expect.that(userCaptor.getValue().getSalt().length()).is(28);
        });

        it.should("hash user password before saving it in the DB", expect -> {
            User user = User.createNewUser("userName", "email@taskroo.com", "fName", "sName", "unencryptedPassword");
            usersService.createUser(user);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(usersDaoMock).createUser(userCaptor.capture());

            expect.that(userCaptor.getValue().getPassword()).is(not(isEmptyOrNullString()));
            expect.that(userCaptor.getValue().getPassword()).is(not(equalTo("unencryptedPassword")));
        });
    });
}}