package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RolColSumTest {

    @Test
    public void whenSumThenCorrect() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] expected = {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        assertArrayEquals(expected, RolColSum.sum(matrix));
    }

    @Test
    public void whenAsyncSumThenCorrect() throws InterruptedException {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] expected = new Sums[]{
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        assertArrayEquals(expected, RolColSum.asyncSum(matrix));
    }

    @Test
    public void whenSingleElementMatrixThenCorrect() {
        int[][] matrix = {
                {5}
        };

        Sums[] expected = new Sums[]{
                new Sums(5, 5),
        };
        assertArrayEquals(expected, RolColSum.asyncSum(matrix));
    }
}