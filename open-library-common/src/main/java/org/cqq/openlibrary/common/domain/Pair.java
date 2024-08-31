package org.cqq.openlibrary.common.domain;

import java.util.Map;

/**
 * Key value pair
 *
 * @author CongQingquan
 */
public class Pair<K, V> implements Map.Entry<K, V> {
    
    private K key;
    
    private V value;
    
    public Pair() {
    }
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K setKey(K key) {
        K old = this.key;
        this.key = key;
        return old;
    }
    
    @Override
    public K getKey() {
        return key;
    }
    
    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
    
    @Override
    public V getValue() {
        return value;
    }
    
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
}