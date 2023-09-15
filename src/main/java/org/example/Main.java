package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    String string = generateRoute("RLRFR", 100);
                    int count = 0;
                    for (char el : string.toCharArray()) {
                        if (el == 'R') count++;
                    }
                    synchronized (sizeToFreq) {
                        if (sizeToFreq.containsKey(count)) {
                            sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                        } else {
                            sizeToFreq.put(count, 1);
                        }
                    }
                }).start();
            }
            StringBuilder stringBuilder = new StringBuilder();
            sizeToFreq
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        if (stringBuilder.indexOf("Cамое частое количество повторений ") == -1)
                            stringBuilder.append("Cамое частое количество повторений ").append(entry.getKey()).append(" (встретилось ").append(entry.getValue()).append(" раз)\n").append("Другие размеры:\n");
                        stringBuilder.append("- ").append(entry.getKey()).append(" (").append(entry.getValue()).append(" раз)\n");
                    });
            System.out.println(stringBuilder);
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