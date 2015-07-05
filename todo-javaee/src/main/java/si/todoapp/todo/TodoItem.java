package si.todoapp.todo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.util.Objects;

@Entity
public class TodoItem {

    @Id
    @NotNull
    @Size(min=22, max=22)
    private String id;

    @NotNull(groups = {InputTodoItemGroup.class, Default.class})
    @Size(max=255, groups = {InputTodoItemGroup.class, Default.class})
    private String title;

    @NotNull
    private Long created;

    @NotNull(groups = {InputTodoItemGroup.class, Default.class})
    private Boolean completed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()){
            return false;
        }

        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(this.id, todoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "TodoItem [id=" + id + ", title=" + title + ", created=" + created + ", completed=" + completed + "]";
    }

    public static class Builder {

        private final TodoItem todoItem;

        public Builder() {
            todoItem = new TodoItem();
        }

        public Builder id(String id) {
            todoItem.setId(id);
            return this;
        }

        public Builder title(String title) {
            todoItem.setTitle(title);
            return this;
        }

        public Builder created(Long created) {
            todoItem.setCreated(created);
            return this;
        }

        public Builder completed(Boolean completed) {
            todoItem.setCompleted(completed);
            return this;
        }

        public TodoItem build() {
            return todoItem;
        }
    }

}
