package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParallelSearchTest {

    @Test
    void testSmallArray() {
        Integer[] array = {1, 2, 3, 4, 5};
        assertEquals(2, ParallelSearch.search(array, 3));
    }

    @Test
    void testLargeArray() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i;
        }
        assertEquals(50, ParallelSearch.search(array, 50));
    }

    @Test
    void testNotFound() {
        Integer[] array = {1, 2, 3, 4, 5};
        assertEquals(-1, ParallelSearch.search(array, 10));
    }

    @Test
    void testDifferentTypes() {
        String[] array = {"apple", "banana", "cherry"};
        assertEquals(1, ParallelSearch.search(array, "banana"));
    }
}