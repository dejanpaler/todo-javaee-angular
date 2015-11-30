package com.todoapp.todo;

import com.todoapp.rest.CorsHeaders;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class TodoItemsResourceIT {

  public static final String LOREM_256 = "Lorem ipsum dolor sit amet, solet inermis nominati pro " +
      "id, ut sed zril " +
      "eirmod, ad melius detraxit eos. At sed postea feugait vituperata. Eam ut ceteros " +
      "splendide, duo in modus " +
      "feugiat. Pro ad maiorum splendide. Modo percipitur vix an, dico semper reprehend";
  private static final String TARGET_FOLDER = "target/";
  private static final String ARCHIVE_NAME = "todoapp-javaee.war";
  private static final String TODO_ITEMS_URI = "http://localhost:8181/todoapp-javaee/todo-items";
  private static Random random;

  private static WebTarget todoResource;

  private static List<String> createdTodos = new ArrayList<>();

  @BeforeClass
  public static void initializeRestClient() {
    random = new Random();
    Client client = ClientBuilder.newClient();
    todoResource = client.target(TODO_ITEMS_URI);
  }

  @AfterClass
  public static void cleanUp() {
    createdTodos.forEach(createdTodo -> {
          Response response = todoResource
              .path(createdTodo)
              .request(MediaType.APPLICATION_JSON)
              .delete();
          System.err.println(response.getStatus());
        }
    );
  }

  @Deployment
  public static WebArchive createDeploymentArchive() {
    return ShrinkWrap.create(ZipImporter.class, ARCHIVE_NAME)
        .importFrom(new File(TARGET_FOLDER + ARCHIVE_NAME))
        .as(WebArchive.class);
  }

  @Test
  public void createTodoRespondsWithCreatedTodo() throws Exception {
    TodoItem randomTodo = buildRandomTodo();
    TodoItem responseTodoItem = postTodo(randomTodo);

    assertPropertiesNotNull(responseTodoItem);
    assertThat(randomTodo.getCompleted(), is((equalTo(responseTodoItem.getCompleted()))));
    assertThat(randomTodo.getTitle(), is((equalTo(responseTodoItem.getTitle()))));
  }

  @Test
  public void createTodoRespondsWithCreatedStatusCodeAndLocation() throws Exception {
    Response response = todoResource
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.json(buildRandomTodo()));
    TodoItem todoItem = response.readEntity(TodoItem.class);
    createdTodos.add(todoItem.getId());

    assertThat(response.getLocation().toString(), is(equalTo(TODO_ITEMS_URI + "/" + todoItem
        .getId())));
    assertThat(response.getStatus(), is(equalTo(Status.CREATED.getStatusCode())));
  }

  @Test
  public void createInvalidTitleTodoRespondsWithBadRequest() throws Exception {
    TodoItem todoItem = buildRandomTodo();
    todoItem.setTitle(LOREM_256);
    Response response = todoResource.request(MediaType.APPLICATION_JSON)
        .post(Entity.json(todoItem));

    assertThat(response.getStatus(), is(equalTo(Status.BAD_REQUEST.getStatusCode())));
  }

  @Test
  public void createInvalidCompletedTodoRespondsWithBadRequest() throws Exception {
    JsonObject jsonTodo = Json.createObjectBuilder()
        .add("title", "test title")
        .add("completed", "invalid-false")
        .build();

    Response response = todoResource.request(MediaType.APPLICATION_JSON).post(Entity.json
        (jsonTodo));
    assertThat(response.getStatus(), is(equalTo(Status.BAD_REQUEST.getStatusCode())));
  }

  @Test
  public void readTodosRespondsWithAllTodos() throws Exception {
    List<TodoItem> randomTodos = new ArrayList<>();
    IntStream.rangeClosed(1, 3)
        .forEach(i -> randomTodos.add(postRandomTodo()));

    List<TodoItem> serverTodos = todoResource.request(MediaType.APPLICATION_JSON)
        .get(new GenericType<List<TodoItem>>() {
        });

    assertThat(serverTodos.containsAll(randomTodos), is(true));
  }

  @Test
  public void readTodosRespondsWithOkStatusCode() throws Exception {
    Response response = todoResource.request(MediaType.APPLICATION_JSON).get();

    assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
  }

  @Test
  public void readTodoRespondsWithRequestedTodo() throws Exception {
    TodoItem randomTodo = postRandomTodo();
    TodoItem createdTodo = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .get(TodoItem.class);

    assertTodoProperties(randomTodo, createdTodo);
    assertPropertiesNotNull(createdTodo);
  }

  @Test
  public void readTodoRespondsWithOkStatusCode() throws Exception {
    TodoItem randomTodo = postRandomTodo();
    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .get();

    assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
  }

  @Test
  public void readNonexistendTodoRespondsWithNotFound() throws Exception {
    Response response = todoResource.path("FpQ6eIohS2CfbeVn46XmuA")
        .request(MediaType.APPLICATION_JSON)
        .get();

    assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
  }

  @Test
  public void readInvalidTodoRespondsWithBadRequest() throws Exception {
    Response response = todoResource.path("_FpQ6eIohS2CfbeVn46XmuA")
        .request()
        .get();

    assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
  }

  @Test
  public void updateTodoRespondsWithUpdatedTodo() throws Exception {
    TodoItem randomTodo = postRandomTodoForUpdate();

    TodoItem updatedTodo = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .put(Entity.json(randomTodo), TodoItem.class);

    assertTodoProperties(randomTodo, updatedTodo);
    assertPropertiesNotNull(updatedTodo);
  }

  @Test
  public void updateTodoRespondsWithOkStatusCode() throws Exception {
    TodoItem randomTodo = postRandomTodoForUpdate();

    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .put(Entity.json(randomTodo));

    assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
  }

  @Test
  public void updateNonexistendTodoRespondsWithNotFound() throws Exception {
    TodoItem randomTodo = postRandomTodoForUpdate();

    Response response = todoResource.path(TodoItemId.generate())
        .request(MediaType.APPLICATION_JSON)
        .put(Entity.json(randomTodo));

    assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
  }

  @Test
  public void updateInvalidTodoRespondsWithBadRequest() throws Exception {
    TodoItem randomTodo = postRandomTodoForUpdate();

    Response response = todoResource.path(TodoItemId.generate() + "test")
        .request(MediaType.APPLICATION_JSON)
        .put(Entity.json(randomTodo));

    assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
  }

  @Test
  public void deleteRespondsWithDeletedTodo() throws Exception {
    TodoItem randomTodo = postRandomTodoForDelete(buildRandomTodo());

    TodoItem deletedTodo = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .delete(TodoItem.class);
    List<TodoItem> serverTodos = todoResource.request(MediaType.APPLICATION_JSON)
        .get(new GenericType<List<TodoItem>>() {
        });

    assertTodoProperties(randomTodo, deletedTodo);
    assertThat(serverTodos.contains(randomTodo), is(false));
    assertPropertiesNotNull(deletedTodo);
  }

  @Test
  public void deleteRespondsWithOkStatusCode() throws Exception {
    TodoItem randomTodo = postRandomTodoForDelete(buildRandomTodo());

    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .delete();

    assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
  }

  @Test
  public void deleteNonexistendTodoRespondsWithNotFound() throws Exception {
    Response response = todoResource.path(TodoItemId.generate())
        .request(MediaType.APPLICATION_JSON)
        .delete();

    assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
  }

  @Test
  public void deleteInvalidTodoRespondsWithBadRequest() throws Exception {
    Response response = todoResource.path(TodoItemId.generate() + "test")
        .request(MediaType.APPLICATION_JSON)
        .delete();

    assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
  }

  @Test
  public void createTodoItemShouldRepondWithCorsHeaders() {
    Response response = todoResource.request(MediaType.APPLICATION_JSON)
        .post(Entity.json(buildRandomTodo()));

    createdTodos.add(response.readEntity(TodoItem.class).getId());

    assertCorsHeaders(response);
  }

  @Test
  public void readAllTodosShouldRepondWithCorsHeaders() {
    Response response = todoResource.request(MediaType.APPLICATION_JSON)
        .get();

    assertCorsHeaders(response);
  }

  @Test
  public void readTodoShouldRepondWithCorsHeaders() {
    TodoItem randomTodo = postRandomTodo();
    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .get();

    assertCorsHeaders(response);
  }

  @Test
  public void updateTodoShouldRepondWithCorsHeaders() {
    TodoItem randomTodo = postRandomTodoForUpdate();

    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .put(Entity.json(randomTodo));

    assertCorsHeaders(response);
  }

  @Test
  public void deleteShouldRespondsWithCorsHeaders() throws Exception {
    TodoItem randomTodo = postRandomTodoForDelete(buildRandomTodo());

    Response response = todoResource.path(randomTodo.getId())
        .request(MediaType.APPLICATION_JSON)
        .delete();

    assertCorsHeaders(response);
  }

  private void assertCorsHeaders(Response response) {
    assertThat(
        response.getHeaderString(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY),
        is(equalTo(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE)));

    assertThat(
        response.getHeaderString(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS_KEY),
        is(equalTo(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS_VALUE)));

    assertThat(
        response.getHeaderString(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS_KEY),
        is(equalTo(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS_VALUE)));

    assertThat(
        response.getHeaderString(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN_KEY),
        is(equalTo(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN_VALUE)));

    assertThat(
        response.getHeaderString(CorsHeaders.ACCESS_CONTROL_MAX_AGE_KEY),
        is(equalTo(CorsHeaders.ACCESS_CONTROL_MAX_AGE_VALUE)));
  }

  private void assertTodoProperties(TodoItem randomTodo, TodoItem updatedTodo) {
    assertThat(randomTodo, is((equalTo(updatedTodo))));
    assertThat(randomTodo.getCompleted(), is((equalTo(updatedTodo.getCompleted()))));
    assertThat(randomTodo.getTitle(), is((equalTo(updatedTodo.getTitle()))));
    assertThat(randomTodo.getId(), is((equalTo(updatedTodo.getId()))));
    assertThat(randomTodo.getCreated(), is((equalTo(updatedTodo.getCreated()))));
  }

  private void assertPropertiesNotNull(TodoItem todoItem) {
    assertThat(todoItem.getId(), notNullValue());
    assertThat(todoItem.getCreated(), notNullValue());
    assertThat(todoItem.getCompleted(), notNullValue());
    assertThat(todoItem.getTitle(), notNullValue());
  }

  private TodoItem postRandomTodoForDelete(TodoItem todoItem) {
    return todoResource
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.json(todoItem), TodoItem.class);
  }

  private TodoItem postRandomTodoForUpdate() {
    TodoItem randomTodo = postRandomTodo();
    randomTodo.setCompleted(!randomTodo.getCompleted());
    randomTodo.setTitle(TodoItemId.generate());
    return randomTodo;
  }

  private TodoItem postRandomTodo() {
    return postTodo(buildRandomTodo());
  }

  private TodoItem postTodo(TodoItem todoItem) {
    TodoItem createdTodoItem = todoResource
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.json(todoItem), TodoItem.class);
    createdTodos.add(createdTodoItem.getId());
    return createdTodoItem;
  }

  private TodoItem buildRandomTodo() {
    return new TodoItem.Builder()
        .completed(random.nextBoolean())
        .title("test.todo.item-" + TodoItemId.generate())
        .build();
  }
}
