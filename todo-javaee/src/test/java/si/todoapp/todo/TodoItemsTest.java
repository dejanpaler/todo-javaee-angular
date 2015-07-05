package si.todoapp.todo;

import org.junit.Before;
import org.junit.Test;
import si.todoapp.logging.Log;
import si.todoapp.startup.StartupEvent;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TodoItemsTest {

    private Log log = mock(Log.class);

    private EntityManager entityManager = mock(EntityManager.class);

    private TodoItems todoItems;

    @Before
    public void initializeTodoItems(){
        todoItems = new TodoItems();
        todoItems.log = log;
        todoItems.entityManager = entityManager;
    }

    @Test
    public void shouldCreateSampleTodoItems() throws Exception {
        todoItems.createSampleTodoItems(new StartupEvent(){});

        verify(entityManager, times(5)).persist(any(TodoItem.class));
    }

    @Test
    public void shouldCreateTodoItem() throws Exception {
        final TodoItem createdTodoItem = todoItems.createTodoItem("todo", false);

        verify(entityManager, times(1)).persist(createdTodoItem);
        assertThat(createdTodoItem.getTitle(), is(equalTo("todo")));
        assertThat(createdTodoItem.getCompleted(), is(equalTo(false)));
    }

    @Test
    public void shouldRetrieveAllTodoItems() throws Exception {
        final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        final CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        final Root root = mock(Root.class);
        final CriteriaQuery all = mock(CriteriaQuery.class);
        final TypedQuery allQuery = mock(TypedQuery.class);

        doReturn(criteriaBuilder).when(entityManager).getCriteriaBuilder();
        doReturn(criteriaQuery).when(criteriaBuilder).createQuery(TodoItem.class);
        doReturn(root).when(criteriaQuery).from(TodoItem.class);
        doReturn(all).when(criteriaQuery).select(root);
        doReturn(allQuery).when(entityManager).createQuery(all);
        doReturn(new ArrayList<TodoItem>()).when(allQuery).getResultList();

        final Collection<TodoItem> allTodoItems = todoItems.getAllTodoItems();

        verify(entityManager, times(1)).createQuery(all);
        assertThat(allTodoItems.size(), is(equalTo(0)));
    }

    @Test
    public void shouldGetTodoItem() throws Exception {
        TodoItem todoItem = new TodoItem.Builder()
                .id("todoId")
                .title("todo")
                .build();

        doReturn(todoItem).when(entityManager).find(TodoItem.class, todoItem.getId());

        final Optional<TodoItem> optionalTodoItem = todoItems.getTodoItem("todoId");

        verify(entityManager).find(TodoItem.class, "todoId");
        assertThat(optionalTodoItem.get(), is(equalTo(todoItem)));
    }

    @Test
    public void shouldGetEmptyOptionalWhenGetTodoItemIsNonexistent() throws Exception {
        doReturn(null).when(entityManager).find(TodoItem.class, "todoId");

        final Optional<TodoItem> optionalTodoItem = todoItems.getTodoItem("todoId");

        verify(entityManager).find(TodoItem.class, "todoId");
        assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
    }

    @Test
    public void shouldDeleteTodoItem() throws Exception {
        TodoItem todoItem = new TodoItem.Builder()
                .id("todoId")
                .title("todo")
                .build();
        doReturn(todoItem).when(entityManager).find(TodoItem.class, todoItem.getId());

        final Optional<TodoItem> optionalTodoItem = todoItems.deleteTodoItem("todoId");

        verify(entityManager).find(TodoItem.class, "todoId");
        verify(entityManager).remove(todoItem);
        assertThat(optionalTodoItem.get(), is(equalTo(todoItem)));
    }

    @Test
    public void shouldNotDeleteNonexistentTodoItem() throws Exception {
        doReturn(null).when(entityManager).find(TodoItem.class, "todoId");

        final Optional<TodoItem> optionalTodoItem = todoItems.deleteTodoItem("todoId");

        verify(entityManager).find(TodoItem.class, "todoId");
        assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
    }

    @Test
    public void shouldUpdateTodoItem() throws Exception {
        TodoItem todoItem = new TodoItem.Builder()
                .id("todoId")
                .title("todo")
                .completed(true)
                .build();
        doReturn(todoItem).when(entityManager).find(TodoItem.class, todoItem.getId());

        final Optional<TodoItem> optionalTodoItem = todoItems
                .updateTodoItem("todoId", "updated title", false);


        verify(entityManager).find(TodoItem.class, "todoId");
        assertThat(optionalTodoItem.get().getTitle(), is(equalTo("updated title")));
        assertThat(optionalTodoItem.get().getCompleted(), is(equalTo(false)));
    }

    @Test
    public void shouldNotUpdateNonexistentTodoItem() throws Exception {
        doReturn(null).when(entityManager).find(TodoItem.class, "todoId");

        final Optional<TodoItem> optionalTodoItem = todoItems.updateTodoItem("todoId", "todo title", false);

        verify(entityManager).find(TodoItem.class, "todoId");
        assertThat(optionalTodoItem.isPresent(), is(equalTo(false)));
    }
}
