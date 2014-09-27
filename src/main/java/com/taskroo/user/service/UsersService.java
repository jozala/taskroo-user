package com.taskroo.user.service;

import com.taskroo.user.data.UserDao;
import com.taskroo.user.domain.factory.SafeUserFactory;
import com.taskroo.user.domain.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Component
@Path("/")
@Api(value = "/", description = "User account management")
public class UsersService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final UserDao usersDao;
    private final SafeUserFactory safeUserFactory;

    @Inject
    public UsersService(UserDao usersDao, SafeUserFactory safeUserFactory) {
        this.usersDao = usersDao;
        this.safeUserFactory = safeUserFactory;
    }

    @Path("/signup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates user account", notes = "Returns details about created user account", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User account created correctly."),
            @ApiResponse(code = 400, message = "Invalid data. It was not possible to create account.")})
    public Response createUser(@Valid User user) {
        LOGGER.debug("SignUp request received with user login: ");
        if (usersDao.exists(user.get_id())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User userWithHashedPassword = safeUserFactory.createUserWithHashedPassword(user);
        usersDao.createUser(userWithHashedPassword);
        return Response.created(URI.create("/"+ user.get_id())).build();
    }
}
