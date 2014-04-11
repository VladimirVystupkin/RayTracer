package com.spbstu.raytracing;

import com.sun.javafx.beans.annotations.NonNull;

/**
 * Class for making object relation like {@link java.util.Map.Entry}
 *
 * @autor vva
 */
public class Relation<K, V> {
    final K key;
    final V value;

    /**
     * Constructor to make relation by pair (key and value)
     *
     * @param key   relation key
     * @param value relation value
     */
    public Relation(@NonNull final K key,@NonNull final V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns relation key
     * @return relation key
     */
    @NonNull
    public K getKey() {
        return key;
    }

    /**
     * Returns relation value
     * @return relation value
     */
    @NonNull
    public V getValue() {
        return value;
    }


}
