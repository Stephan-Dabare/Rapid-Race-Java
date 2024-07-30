package org.example.javacw;

public class CustomSort {
    public static void selectionSort(Horse[] horses) {
        // Get the length of horse array.
        int n = horses.length;
        // Selection sort algorithm.
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                // Check current time less than min time respective to indexes.
                if (horses[j].getRaceTime() < horses[minIndex].getRaceTime()) {
                    minIndex = j;
                }
            }
            // Swap horses[i] index with horses[minIndex] min index.
            Horse temp = horses[minIndex];
            horses[minIndex] = horses[i];
            horses[i] = temp;
        }
    }
}

