package com.spbstu.raytracing;



/**
 * @author vva
 */
public class Relation<K, V> {
    final K key;
    final V value;

    public Relation(final K key, final V value) {
        this.key = key;
        this.value = value;
    }


    public K getKey() {
        return key;
    }


    public V getValue() {
        return value;
    }
}
