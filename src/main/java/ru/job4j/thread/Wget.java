package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private static final String FILE_NAME = "downloaded_file";

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                long startTime = System.currentTimeMillis();

                fileOutputStream.write(buffer, 0, bytesRead);
                System.out.printf("Downloaded: %d bytes%n", bytesRead);

                long timeTaken = System.currentTimeMillis() - startTime;
                long expectedTime = (long) (1000.0 * bytesRead / speed);

                long sleepTime = expectedTime - timeTaken;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }
            System.out.println("Download complete!");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Download error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: java Wget <URL> <speed>");
            return;
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}