package si.todoapp.todo;

import si.todoapp.logging.Log;
import si.todoapp.startup.StartupEvent;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Stateless
public class TodoItems {

    private static final int SAMPLE_BOUND = 100;

    @Inject
    Log log;

    @PersistenceContext
    EntityManager entityManager;

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
                this.entityManager.persist(todoItem);
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
        this.entityManager.persist(createdTodoItem);
        return createdTodoItem;
    }

    public Collection<TodoItem> getAllTodoItems() {
        // Return all todos using criteria builder. Another option is to use named query.
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoItem> cq = cb.createQuery(TodoItem.class);
        Root<TodoItem> rootEntry = cq.from(TodoItem.class);
        CriteriaQuery<TodoItem> all = cq.select(rootEntry);

        TypedQuery<TodoItem> allQuery = this.entityManager.createQuery(all);
        allQuery.setMaxResults(100);

        return allQuery.getResultList();
    }

    public Optional<TodoItem> getTodoItem(String todoItemId) {
        return Optional.ofNullable(this.entityManager.find(TodoItem.class, todoItemId));
    }

    public Optional<TodoItem> deleteTodoItem(String todoItemId) {
        final Optional<TodoItem> todoItemOptional = this.getTodoItem(todoItemId);

        if(todoItemOptional.isPresent()){
            this.entityManager.remove(todoItemOptional.get());
        }

        return  todoItemOptional;
    }

    public Optional<TodoItem> updateTodoItem(String todoItemId, String title, boolean completed) {
        final Optional<TodoItem> todoItemOptional = this.getTodoItem(todoItemId);

        if(todoItemOptional.isPresent()){
            final TodoItem storedTodoItem = todoItemOptional.get();
            storedTodoItem.setCompleted(completed);
            storedTodoItem.setTitle(title);
        }

        return todoItemOptional;
    }

}
