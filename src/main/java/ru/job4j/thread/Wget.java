package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String fileName;

    public Wget(String url, int speed) throws MalformedURLException {
        this.url = url;
        this.speed = speed;
        this.fileName = new URL(url).getPath().substring(url.lastIndexOf('/') + 1);
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long startTime = System.currentTimeMillis();
            long totalBytes = 0;

            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;

                if (totalBytes >= speed) {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    if (timeTaken < 1000) {
                        Thread.sleep(1000 - timeTaken);
                    }
                    totalBytes = 0;
                    startTime = System.currentTimeMillis();
                }
            }
            System.out.println("Download complete!");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Download error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java Wget <URL> <speed>");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}