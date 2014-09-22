package com.taskroo.user.data;

import com.taskroo.user.domain.User;
import org.jongo.MongoCollection;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Repository
public class UserDao {

    private final MongoCollection usersCollection;

    @Inject
    public UserDao(MongoCollection usersCollection) {
        this.usersCollection = usersCollection;
    }

    public User createUser(User user) {
        if (user.getSalt() == null) {
            throw new InvalidDataOperationException("Trying to save user: " + user.get_id() + " without salt (null)");
        }
        usersCollection.insert(user);
        return user;
    }

    public boolean exists(String username) {
        return usersCollection.count("{_id: #}", username) > 0;
    }
}
