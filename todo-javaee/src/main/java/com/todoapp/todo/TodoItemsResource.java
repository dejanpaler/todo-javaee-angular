package com.todoapp.todo;

import com.todoapp.rest.ResourceLogged;

import java.net.URI;
import java.util.Collection;

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

@Path("/todo-items")
@ResourceLogged
public class TodoItemsResource {

  @Inject
  TodoItems todoItems;

  /**
   * Exposed resource method for creating todo items.
   *
   * @param todoItem - todo item object
   * @param uriInfo - information for the URI
   * @return response object
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response createTodoItem(
      @Valid
      @NotNull
      @ConvertGroup(from = Default.class, to = InputTodoItemGroup.class)
      TodoItem todoItem,
      @Context UriInfo uriInfo) {

    TodoItem createdTodoItem = todoItems.createTodoItem(todoItem.getTitle(), todoItem
        .getCompleted());
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

  /**
   * Returns a collection of all todo items.
   *
   * @return collection of all todo items
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTodoItems() {
    final Collection<TodoItem> allTodoItems = todoItems.getAllTodoItems();
    GenericEntity<Collection<TodoItem>> list =
        new GenericEntity<Collection<TodoItem>>(allTodoItems) {};

    return Response
      .ok(list)
      .build();
  }

  /**
   * Returns todo item with provided id.
   *
   * @param todoItemId - todo item
   * @return todo item with provided id
   */
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

  /**
   * Updates todo item by provided id, title and completion status.
   *
   * @param todoItemId - id of an todo item that needs to be updated
   * @param todoItem - an updating todo item
   * @return return response object
   */
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

  /**
   * Deletes a todo item based ond the provided id.
   *
   * @param todoItemId - an ide of a todo item that needs to be removed
   * @return response object
   */
  @DELETE
  @Path("/{todo-item-id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteTodoItem(@PathParam("todo-item-id") @Valid @Size(min = 22, max = 22)
                                   final String todoItemId) {
    TodoItem todoItem = todoItems
        .deleteTodoItem(todoItemId)
        .orElseThrow(NotFoundException::new);

    return Response
        .ok(todoItem)
        .build();
  }

}
