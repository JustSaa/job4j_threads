package ru.job4j.pool;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelSearch<T> {
    private static final int THRESHOLD = 10;

    public int search(T[] array, T target) {
        if (array.length <= THRESHOLD) {
            return linearSearch(array, target);
        }
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new SearchTask<>(array, target, 0, array.length - 1));
    }

    private int linearSearch(T[] array, T target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private static class SearchTask<T> extends RecursiveTask<Integer> {
        private final T[] array;
        private final T target;
        private final int start;
        private final int end;

        public SearchTask(T[] array, T target, int start, int end) {
            this.array = array;
            this.target = target;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    if (array[i].equals(target)) {
                        return i;
                    }
                }
                return -1;
            }

            int mid = start + (end - start) / 2;
            SearchTask<T> leftTask = new SearchTask<>(array, target, start, mid);
            SearchTask<T> rightTask = new SearchTask<>(array, target, mid + 1, end);

            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();

            return (leftResult != -1) ? leftResult : rightResult;
        }
    }
}
