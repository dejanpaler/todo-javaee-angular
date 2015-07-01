package si.todoapp.cache;

import si.todoapp.logging.Log;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    public V put(K key, V value) {
        if (cache.size() >= CACHE_SIZE_LIMIT){
            cache.clear();
        }
        return cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public Collection<V> values() {
        return cache.values();
    }

    public V remove(K key) {
        return cache.remove(key);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public int sizeLimit(){
        return MapCacheContainer.CACHE_SIZE_LIMIT;
    }
}
