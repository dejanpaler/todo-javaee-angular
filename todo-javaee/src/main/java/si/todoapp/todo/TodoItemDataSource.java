package si.todoapp.todo;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;

@DataSourceDefinition(
        name = "java:app/todoItem",
        className = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource",
        databaseName = "todo",
        serverName = "localhost",
        portNumber = 3306,
        user = "todo",
        password = "todo")
@Stateless
public class TodoItemDataSource {
}
