package ru.job4j.pool;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T target;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10;

    public ParallelSearch(T[] array, T target, int start, int end) {
        this.array = array;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    public static <T> int search(T[] array, T target) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new ParallelSearch<>(array, target, 0, array.length - 1));
    }

    private int linearSearch() {
        for (int i = start; i <= end; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            return linearSearch();
        }

        int mid = start + (end - start) / 2;
        ParallelSearch<T> leftTask = new ParallelSearch<>(array, target, start, mid);
        ParallelSearch<T> rightTask = new ParallelSearch<>(array, target, mid + 1, end);

        leftTask.fork();
        int rightResult = rightTask.compute();
        int leftResult = leftTask.join();

        return (leftResult != -1) ? leftResult : rightResult;
    }
}
