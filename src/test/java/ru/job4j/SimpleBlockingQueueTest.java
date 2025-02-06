package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {
    @Test
    void whenProducerConsumerThenCorrectOrder() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        List<Integer> result = new ArrayList<>();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    queue.offer(i);
                    System.out.println("Producer added: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Integer value = queue.poll();
                    result.add(value);
                    System.out.println("Consumer received: " + value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        assertThat(result).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final LinkedList<Integer> buffer = new LinkedList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(4);

        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(
                            i -> {
                                try {
                                    queue.offer(i);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    Thread.currentThread().interrupt();
                                }
                            }
                    );
                }
        );

        Thread consumer = new Thread(
                () -> {
                    try {
                        while (!Thread.currentThread().isInterrupted()) {
                            Integer value = queue.poll();
                            if (value != null) {
                                buffer.add(value);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        );

        producer.start();
        consumer.start();

        producer.join();
        consumer.interrupt();
        consumer.join();

        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }
}