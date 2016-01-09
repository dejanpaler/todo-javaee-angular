package com.todoapp.todo;

import com.todoapp.cache.MapCacheContainer;
import com.todoapp.logging.Log;
import com.todoapp.startup.ApplicationStartupEvent;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Stateless
public class TodoItems {

  @Inject
  Log log;

  @Inject
  MapCacheContainer<String, TodoItem> cacheMap;

  /**
   * Automatically inserts sample todo items when application starts.
   *
   * @param applicationStartupEvent - the event when application is started
   */
  public void createSampleTodoItems(@Observes ApplicationStartupEvent applicationStartupEvent) {
    log.info("Creating sample todo items...");
    IntStream.rangeClosed(1, 5).forEach(this::createRandomTodoItem);
    log.info("Sample todo items created.");
  }

  private void createRandomTodoItem(int item) {
    TodoItem todoItem = createTodoItem("new item " + item, item % 2 == 0);
    log.info("Created sample todo item {0}: {1}", item, todoItem);
  }

  /**
   * Creates a todo item with given title and completion and insert it to the application cache.
   *
   * @param title - a title for the todo item
   * @param completed - completed status for the todo item
   * @return - todo item
   */
  public TodoItem createTodoItem(String title, boolean completed) {
    TodoItem createdTodoItem = new TodoItem.Builder()
        .id(TodoItemId.generate())
        .created(Instant.now().toEpochMilli())
        .completed(completed)
        .title(title)
        .build();
    cacheMap.put(createdTodoItem.getId(), createdTodoItem);
    return createdTodoItem;
  }

  public Collection<TodoItem> getAllTodoItems() {
    return cacheMap.values();
  }

  public Optional<TodoItem> getTodoItem(String todoItemId) {
    return Optional.ofNullable(cacheMap.get(todoItemId));
  }

  /**
   * Removes the todo item by provided id.
   *
   * @param todoItemId - id of a todo item that needs to be removed
   * @return - the removed todo item or empty optional if todo item not found
   */
  public Optional<TodoItem> deleteTodoItem(String todoItemId) {
    return Optional.ofNullable(cacheMap.remove(todoItemId));
  }

  /**
   * Updates a toodo item based on provided id, title and completion status.
   *
   * @param todoItemId - id of an todo item that needs to be updated
   * @param title - an title f
   * @param completed - completion status
   * @return updated todo item or empty optional if todo item not found
   */
  public Optional<TodoItem> updateTodoItem(String todoItemId, String title, boolean completed) {
    Optional<TodoItem> todoItemOptional = Optional.ofNullable(this.cacheMap.get(todoItemId));

    if (todoItemOptional.isPresent()) {
      TodoItem storedTodoItem = todoItemOptional.get();
      storedTodoItem.setCompleted(completed);
      storedTodoItem.setTitle(title);
      return Optional.of(storedTodoItem);
    }

    return todoItemOptional;
  }

}
