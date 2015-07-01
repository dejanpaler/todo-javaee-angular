package si.todoapp.todo;

import si.todoapp.rest.ResourceLogged;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

@Path("/todo-items")
@ResourceLogged
public class TodoItemsResource {

    @Inject
    TodoItems todoItems;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodoItem(
            @Valid
            @NotNull
            @ConvertGroup(from = Default.class, to = InputTodoItemGroup.class)
            TodoItem todoItem,
            @Context UriInfo uriInfo) {

        TodoItem createdTodoItem = todoItems.createTodoItem(todoItem.getTitle(), todoItem.getCompleted());
        URI uri = uriInfo
                .getAbsolutePathBuilder()
                .path("/" + createdTodoItem.getId())
                .build();

        return Response
                .status(Status.CREATED)
                .location(uri)
                .entity(createdTodoItem)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoItems() {
        final Collection<TodoItem> allTodoItems = todoItems.getAllTodoItems();
        GenericEntity<Collection<TodoItem>> list = new GenericEntity<Collection<TodoItem>>(allTodoItems) {
        };

        return Response
                .ok(list)
                .build();
    }

    @GET
    @Path("/{todo-item-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoItem(
            @PathParam("todo-item-id")
            @Valid
            @Size(min = 22, max = 22)
            final String todoItemId) {

        final TodoItem todoItem = todoItems
                .getTodoItem(todoItemId)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(todoItem)
                .build();
    }

    @PUT
    @Path("/{todo-item-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTodoItem(
            @Valid
            @PathParam("todo-item-id")
            @Size(min = 22, max = 22)
            final String todoItemId,
            @Valid
            @NotNull
            @ConvertGroup(from = Default.class, to = InputTodoItemGroup.class)
            TodoItem todoItem) {

        TodoItem createdTodoItem = todoItems
                .updateTodoItem(todoItemId, todoItem.getTitle(), todoItem.getCompleted())
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(createdTodoItem)
                .build();
    }

    @DELETE
    @Path("/{todo-item-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodoItem(@PathParam("todo-item-id") @Valid @Size(min=22, max=22) final String todoItemId) {
        TodoItem todoItem = todoItems
                .deleteTodoItem(todoItemId)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(todoItem)
                .build();
    }

}
