package com.spbstu.raytracing;

/**
 * @autor vystupkin
 * @date 01.04.14
 * @description
 */
public class Relation<K, V> {
    final K key;
    final V value;

    public Relation(K key, V value) {
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
