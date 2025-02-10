package ru.job4j.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RolColSum {
    public static Sums[] sum(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        Sums[] sums = new Sums[rows];
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            sums[i] = new Sums(rowSum, colSum);
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        Sums[] sums = new Sums[rows];

        ExecutorService executor = Executors.newFixedThreadPool(2 * rows);
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            final int rowIndex = i;
            futures.add(executor.submit(() -> {
                int rowSum = 0;
                int colSum = 0;
                for (int j = 0; j < cols; j++) {
                    rowSum += matrix[rowIndex][j];
                    colSum += matrix[j][rowIndex];
                }
                sums[rowIndex] = new Sums(rowSum, colSum);
                return null;
            }));
        }
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return sums;
    }
}