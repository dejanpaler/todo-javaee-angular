package com.todoapp.todo;

import com.todoapp.cache.MapCacheContainer;
import com.todoapp.logging.Log;
import com.todoapp.startup.StartupEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TodoItemsTest {

  private Log log = mock(Log.class);

  @SuppressWarnings("unchecked")
  private MapCacheContainer<String, TodoItem> cacheMap = mock(MapCacheContainer.class);

  private TodoItems todoItems;

  @Before
  public void initializeTodoItems() {
    todoItems = new TodoItems();
    todoItems.log = log;
    todoItems.cacheMap = cacheMap;
  }

  @Test
  public void shouldCreateSampleTodoItems() throws Exception {
    todoItems.createSampleTodoItems(new StartupEvent() {
    });

    verify(cacheMap, times(5)).put(anyString(), any());
  }

  @Test
  public void shouldCreateTodoItem() throws Exception {
    final TodoItem createdTodoItem = todoItems.createTodoItem("todo", false);

    verify(cacheMap, times(1)).put(createdTodoItem.getId(), createdTodoItem);
    assertThat(createdTodoItem.getTitle(), is(equalTo("todo")));
    assertThat(createdTodoItem.getCompleted(), is(equalTo(false)));
  }

  @Test
  public void shouldRetrieveAllTodoItems() throws Exception {
    todoItems.getAllTodoItems();

    verify(cacheMap).values();
  }

  @Test
  public void shouldGetTodoItem() throws Exception {
    TodoItem todoItem = new TodoItem.Builder()
        .id("todoId")
        .title("todo")
        .build();
    doReturn(true).when(cacheMap).containsKey("todoId");
    doReturn(todoItem).when(cacheMap).get("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems.getTodoItem("todoId");

    verify(cacheMap).containsKey("todoId");
    verify(cacheMap).get("todoId");
    assertThat(optionalTodoItem.get(), is(equalTo(todoItem)));
  }

  @Test
  public void shouldGetEmptyOptionalWhenGetTodoItemIsNonexistent() throws Exception {
    doReturn(false).when(cacheMap).containsKey("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems.getTodoItem("todoId");

    verify(cacheMap).containsKey("todoId");
    assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
  }

  @Test
  public void shouldDeleteTodoItem() throws Exception {
    TodoItem todoItem = new TodoItem.Builder()
        .id("todoId")
        .title("todo")
        .build();
    doReturn(true).when(cacheMap).containsKey("todoId");
    doReturn(todoItem).when(cacheMap).remove("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems.deleteTodoItem("todoId");

    verify(cacheMap).containsKey("todoId");
    verify(cacheMap).remove("todoId");
    assertThat(optionalTodoItem.get(), is(equalTo(todoItem)));
  }

  @Test
  public void shouldNotDeleteNonexistentTodoItem() throws Exception {
    doReturn(false).when(cacheMap).containsKey("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems.deleteTodoItem("todoId");

    verify(cacheMap).containsKey("todoId");
    assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
  }

  @Test
  public void shouldUpdateTodoItem() throws Exception {
    TodoItem todoItem = new TodoItem.Builder()
        .id("todoId")
        .title("todo")
        .completed(true)
        .build();
    doReturn(true).when(cacheMap).containsKey("todoId");
    doReturn(todoItem).when(cacheMap).get("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems
        .updateTodoItem("todoId", "updated title", false);

    verify(cacheMap).containsKey("todoId");
    verify(cacheMap).get("todoId");
    assertThat(optionalTodoItem.get().getTitle(), is(equalTo("updated title")));
    assertThat(optionalTodoItem.get().getCompleted(), is(equalTo(false)));
  }

  @Test
  public void shouldNotUpdateNonexistentTodoItem() throws Exception {
    doReturn(false).when(cacheMap).containsKey("todoId");

    final Optional<TodoItem> optionalTodoItem = todoItems.updateTodoItem("todoId", "todo title",
        false);

    verify(cacheMap).containsKey("todoId");
    assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
  }
}
