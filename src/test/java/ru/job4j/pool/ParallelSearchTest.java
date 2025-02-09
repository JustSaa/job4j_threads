package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParallelSearchTest {

    @Test
    void testSmallArray() {
        ParallelSearch<Integer> search = new ParallelSearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        assertEquals(2, search.search(array, 3));
    }

    @Test
    void testLargeArray() {
        ParallelSearch<Integer> search = new ParallelSearch<>();
        Integer[] array = new Integer[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i;
        }
        assertEquals(50, search.search(array, 50));
    }

    @Test
    void testNotFound() {
        ParallelSearch<Integer> search = new ParallelSearch<>();
        Integer[] array = {1, 2, 3, 4, 5};
        assertEquals(-1, search.search(array, 10));
    }

    @Test
    void testDifferentTypes() {
        ParallelSearch<String> search = new ParallelSearch<>();
        String[] array = {"apple", "banana", "cherry"};
        assertEquals(1, search.search(array, "banana"));
    }
}