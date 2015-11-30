package com.todoapp.todo;

import com.sun.messaging.jmq.io.Status;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TodoItemsResourceTest {

  private TodoItemsResource todoItemsResource;

  private TodoItems todoItems = mock(TodoItems.class);

  private String baseUri = "http//example.com/todo_items/";

  @Before
  public void initializeTodoItemsResource() {
    todoItemsResource = new TodoItemsResource();
    todoItemsResource.todoItems = todoItems;
  }

  @Test
  public void shouldCreateTodoItem() throws Exception {
    final TodoItem todoItem = createTodoItem();
    final UriInfo uriInfo = mock(UriInfo.class);
    final UriBuilder uriBuilder = mock(UriBuilder.class);
    final URI todoUri = new URI(baseUri + todoItem.getId());
    given(todoItems.createTodoItem(todoItem.getTitle(), todoItem.getCompleted())).willReturn
            (todoItem);
    given(uriInfo.getAbsolutePathBuilder()).willReturn(uriBuilder);
    given(uriBuilder.path("/" + todoItem.getId())).willReturn(uriBuilder);
    given(uriBuilder.build()).willReturn(todoUri);

    final Response response = todoItemsResource.createTodoItem(todoItem, uriInfo);

    assertThat(response.getStatus(), is(equalTo(Status.CREATED)));
  }

  @Test
  public void shouldRetrieveTodoItems() throws Exception {
    given(todoItems.getAllTodoItems()).willReturn(new ArrayList<>());

    final Response response = todoItemsResource.getTodoItems();

    assertThat(response.getStatus(), is(equalTo(Status.OK)));
  }

  @Test
  public void shouldRetrieveTodoItem() throws Exception {
    final TodoItem todoItem = createTodoItem();
    given(todoItems.getTodoItem(todoItem.getId())).willReturn(Optional.of(todoItem));

    final Response response = todoItemsResource.getTodoItem(todoItem.getId());

    assertThat(response.getStatus(), is(equalTo(Status.OK)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldFailRetrievingNonexistentTodoItem() throws Exception {
    final String todoItemId = TodoItemId.generate();
    given(todoItems.getTodoItem(todoItemId)).willReturn(Optional.empty());

    todoItemsResource.getTodoItem(todoItemId);
  }

  @Test
  public void shouldUpdateTodoItem() throws Exception {
    final TodoItem todoItem = createTodoItem();
    given(todoItems.updateTodoItem(todoItem.getId(), todoItem.getTitle(), todoItem.getCompleted()))
        .willReturn(Optional.of(todoItem));

    final Response response = todoItemsResource.updateTodoItem(todoItem.getId(), todoItem);

    assertThat(response.getStatus(), is(equalTo(Status.OK)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldFailUpdateNonexistentTodoItemIs() throws Exception {
    final TodoItem todoItem = createTodoItem();
    given(todoItems.updateTodoItem(todoItem.getId(), todoItem.getTitle(), todoItem.getCompleted()))
        .willReturn(Optional.empty());

    todoItemsResource.updateTodoItem(todoItem.getId(), todoItem);
  }

  @Test
  public void shouldDeleteTodoItem() throws Exception {
    final TodoItem todoItem = createTodoItem();
    given(todoItems.deleteTodoItem(todoItem.getId())).willReturn(Optional.of(todoItem));

    final Response response = todoItemsResource.deleteTodoItem(todoItem.getId());

    assertThat(response.getStatus(), is(equalTo(Status.OK)));
  }

  @Test(expected = NotFoundException.class)
  public void shouldFailDeleteNonexistentTodoItem() throws Exception {
    final String todoItemId = TodoItemId.generate();
    given(todoItems.deleteTodoItem(todoItemId)).willReturn(Optional.empty());

    todoItemsResource.deleteTodoItem(todoItemId);
  }

  private TodoItem createTodoItem() {
    return new TodoItem.Builder()
        .id(TodoItemId.generate())
        .title("some todo")
        .completed(false)
        .created(Instant.now().toEpochMilli())
        .build();
  }
}
