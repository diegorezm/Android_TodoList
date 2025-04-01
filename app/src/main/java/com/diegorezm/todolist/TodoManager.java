package com.diegorezm.todolist;

import java.util.ArrayList;
import java.util.List;

public class TodoManager<T extends Todo> {
    private final List<T> todos = new ArrayList<>();
    private Filter filter = Filter.UNDONE;

    public void addTodo(T todo) {
        todos.add(todo);
    }

    public List<T> getTodos() {
        return getFilteredTodos();
    }

    public Filter getFilter() {
        return filter;
    }

    private List<T> getFilteredTodos() {
        switch (filter) {
            case ALL:
                return todos;
            case UNDONE:
                List<T> undoneTodos = new ArrayList<>();
                for (T todo : todos) {
                    if (!todo.isCompleted() && !todo.isExpired()) {
                        undoneTodos.add(todo);
                    }
                }
                return undoneTodos;
            case DONE:
                List<T> doneTodos = new ArrayList<>();
                for (T todo : todos) {
                    if (todo.isCompleted()) {
                        doneTodos.add(todo);
                    }
                }
                return doneTodos;

            case EXPIRED:
                List<T> expiredTodos = new ArrayList<>();
                for (T todo : todos) {
                    if (todo.isExpired() && !todo.isCompleted()) {
                        expiredTodos.add(todo);
                    }
                }
                return expiredTodos;
        }
        return java.util.Collections.emptyList();
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }


}
