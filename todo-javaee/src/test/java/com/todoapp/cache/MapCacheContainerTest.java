package com.todoapp.cache;

import com.todoapp.logging.Log;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MapCacheContainerTest {

  Log log = mock(Log.class);

  @Test
  public void shouldInitializeMapCache() throws Exception {
    MapCacheContainer<String, String> cache = new MapCacheContainer<>();
    cache.log = log;
    cache.initializeMapCache();

    cache.put("key", "value");

    assertThat(cache.get("key"), is(equalTo("value")));
    assertThat(cache.values().size(), is(equalTo(1)));
  }

  @Test
  public void shouldClearCacheWhenCacheLimitExceeded() throws Exception {
    MapCacheContainer<String, String> cache = new MapCacheContainer<>();
    cache.log = log;
    cache.initializeMapCache();

    IntStream.rangeClosed(1, cache.sizeLimit() + 1).forEach(i -> cache.put("key" + i, "value" + i));

    assertThat(cache.values().size(), is(equalTo(1)));
  }

  @Test
  public void shouldNotClearCacheWhenCacheLimitNotExceeded() throws Exception {
    MapCacheContainer<String, String> cache = new MapCacheContainer<>();
    cache.log = log;
    cache.initializeMapCache();

    IntStream.rangeClosed(1, cache.sizeLimit()).forEach(i -> cache.put("key" + i, "value" + i));

    assertThat(cache.values().size(), is(equalTo(cache.sizeLimit())));
  }

  @Test
  public void shouldRemoveItem() throws Exception {
    MapCacheContainer<String, String> cache = new MapCacheContainer<>();
    cache.log = log;
    cache.initializeMapCache();

    cache.put("key", "value");
    cache.remove("key");

    assertThat(cache.values().size(), is(equalTo(0)));
    assertThat(cache.containsKey("key"), is(equalTo(false)));
  }

  @Test
  public void shouldNotRemoveNonexistentItem() throws Exception {
    MapCacheContainer<String, String> cache = new MapCacheContainer<>();
    cache.log = log;
    cache.initializeMapCache();

    cache.put("key", "value");
    String removedItem = cache.remove("nonexistent_key");

    assertThat(cache.values().size(), is(equalTo(1)));
    assertThat(cache.containsKey("key"), is(equalTo(true)));
    assertThat(removedItem, is(equalTo(null)));
  }

}
