package com.todoapp.todo;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TodoItemIdTest {

  @Test
  public void shouldGenerateUniqueIds() {

    int expectedNumberOfIds = 10000;

    Set<String> actualIds = new HashSet<>();
    IntStream.rangeClosed(1, expectedNumberOfIds).forEach(i -> actualIds.add(TodoItemId.generate
        ()));

    assertEquals(expectedNumberOfIds, actualIds.size());
  }

}
