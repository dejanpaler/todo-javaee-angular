package si.todoapp.todo;

import si.todoapp.cache.MapCacheContainer;
import si.todoapp.logging.Log;
import si.todoapp.startup.StartupEvent;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Stateless
public class TodoItems {

    public static final int SAMPLE_BOUND = 100;

    @Inject
    Log log;

    @Inject
    MapCacheContainer<String, TodoItem> cacheMap;

    private final Random random = new Random(System.currentTimeMillis());

    public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
        log.info("Creating sample todo items...");

        IntStream.rangeClosed(1, 5).forEach(
            item -> {
                TodoItem todoItem = new TodoItem.Builder()
                    .id(TodoItemId.generate())
                    .created(Instant.now().toEpochMilli())
                    .completed(random.nextBoolean())
                    .title("new item " + random.nextInt(SAMPLE_BOUND))
                    .build();
                cacheMap.put(todoItem.getId(), todoItem);
                log.info("Created sample todo item {0}: {1}", item, todoItem);
            });

        log.info("Sample todo items created.");
    }

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
        return  cacheMap.containsKey(todoItemId) ? Optional.of(cacheMap.get(todoItemId)) : Optional.empty();
    }

    public Optional<TodoItem> deleteTodoItem(String todoItemId) {
        return  cacheMap.containsKey(todoItemId) ? Optional.of(cacheMap.remove(todoItemId)) : Optional.empty();
    }

    public Optional<TodoItem> updateTodoItem(String todoItemId, String title, boolean completed) {
        if(cacheMap.containsKey(todoItemId)){
            TodoItem storedTodoItem = this.cacheMap.get(todoItemId);
            storedTodoItem.setCompleted(completed);
            storedTodoItem.setTitle(title);
            return Optional.of(storedTodoItem);
        }
        return Optional.empty();
    }

}
