package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    synchronized (sizeToFreq) {
                        String string = generateRoute("RLRFR", 100);
                        int count = 0;
                        for (char el : string.toCharArray()) {
                            if (el == 'R') count++;
                        }
                        if (sizeToFreq.containsKey(count)) {
                            sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                        } else {
                            sizeToFreq.put(count, 1);
                        }
                        sizeToFreq.notify();

                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            AtomicBoolean max = new AtomicBoolean(true);
            sizeToFreq
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        if (max.get()) System.out.println(
                                "Cамое частое количество повторений " + entry.getKey() + " (встретилось " + entry.getValue() + " раз)\n"
                                        + "Другие размеры:"
                        );
                        System.out.println(
                                "- " + entry.getKey() + " (" + entry.getValue() + " раз)"
                        );
                        max.set(false);
                    });
        }).start();

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }


}