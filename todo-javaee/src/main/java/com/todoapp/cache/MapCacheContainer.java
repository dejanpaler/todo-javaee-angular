package com.todoapp.cache;

import com.todoapp.logging.Log;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MapCacheContainer<K, V> {

  private static final int CACHE_SIZE_LIMIT = 100;

  @Inject
  Log log;

  private ConcurrentMap<K, V> cache;

  @PostConstruct
  public void initializeMapCache() {
    this.cache = new ConcurrentHashMap<>();
    log.info("Map cache initialized.");
  }

  /**
   * Associates the specified value with the specified key in this cache. Cache is automatically
   * cleared when the cache size limit is reached.
   */
  public V put(K key, V value) {
    if (cache.size() >= CACHE_SIZE_LIMIT) {
      cache.clear();
    }
    return cache.put(key, value);
  }

  /**
   * Returns the value to which the specified key is associated, or null if this cache contains no
   * association for the key.
   *
   * @return the value to which the specified key is associated
   */
  public V get(K key) {
    return cache.get(key);
  }

  /**
   * Returns a Collection view of the values contained in this cache.
   *
   * @return a Collection view of the values contained in this cache
   */
  public Collection<V> values() {
    return cache.values();
  }

  /**
   * Removes the association for a key from this cache if it is present.
   *
   * @param key - key whose association is to be removed from the cache
   * @return the previous value associated with key, or null if there was no association for key
   */
  public V remove(K key) {
    return cache.remove(key);
  }

  /**
   * Returns true if this cache contains a mapping for the specified key.
   *
   * @param key - key whose presence in this cache is to be tested
   * @return true if this cache contains a association for the specified key
   */
  public boolean containsKey(K key) {
    return cache.containsKey(key);
  }

  /**
   * Returns the size limit of this cache.
   *
   * @return the size limit of this cache
   */
  public int sizeLimit() {
    return MapCacheContainer.CACHE_SIZE_LIMIT;
  }
}
