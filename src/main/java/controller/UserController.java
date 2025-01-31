package controller;

import dto.user.request.UserDtoRequest;
import dto.user.response.*;
import entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.user.UserService;

import java.util.Collections;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    // Створення нового запису
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(final UserDtoRequest request) {
        User user = userService.create(request);
        if (user != null)
            return Response.ok()
                    .entity(UserDtoCreateResponse.of(true, user))
                    .build();
        else
            return Response.ok()
                    .entity(UserDtoCreateResponse.of(false, null))
                    .build();
    }

    // Отримання всіх записів
    @GET
    public Response getAllUsers() {
        List<User> list = userService.getAll();
        if (list.isEmpty())
            return Response.ok()
                    .entity(UserDtoListResponse.of(true, Collections.emptyList()))
                    .build();
        else
            return Response.ok()
                    .entity(UserDtoListResponse.of(false, list))
                    .build();
    }


    // ---- Path Params ----------------------

    // Отримання запису за id
    @GET
    @Path("{id: [1-9][0-9]*}")
    public Response getUserById(@PathParam("id") final Long id) {
        User user = userService.getById(id);
        if (user != null)
            return Response.ok()
                    .entity(UserDtoByIdResponse.of(id, true, user))
                    .build();
        else
            return Response.ok()
                    .entity(UserDtoByIdResponse.of(id, false, null))
                    .build();
    }

    // Оновлення запису за id
    @PUT
    @Path("{id: [0-9]+}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUserById(@PathParam("id") final Long id, final UserDtoRequest request) {
        User userToUpdate = userService.getById(id);
        if (userToUpdate != null) {
            User userUpdated = userService.update(id, request);
            return Response.ok()
                    .entity(UserDtoUpdateResponse.of(id, true, userUpdated))
                    .build();
        } else {
            return Response.ok()
                    .entity(UserDtoUpdateResponse.of(id, false, null))
                    .build();
        }
    }

    // Видалення запису за id
    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteUserById(@PathParam("id") final Long id) {
        if (userService.deleteById(id))
            return Response.ok()
                    .entity(UserDtoDeleteResponse.of(id, true))
                    .build();
        else
            return Response.ok()
                    .entity(UserDtoDeleteResponse.of(id, false))
                    .build();
    }


}
