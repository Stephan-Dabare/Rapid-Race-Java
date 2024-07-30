package org.example.javacw;

public class CustomSortView {
    public static void selectionSort(Horse[] horses) {
        // Get the length of the array
        int n = horses.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                // Compare horse IDs as integers according to indexes.
                if (compareHorseIDs(horses[j].getHorseID(), horses[minIndex].getHorseID()) < 0) {
                    minIndex = j;
                }
            }
            // Swap horses[i] (current ID) with horses[minIndex] (Min ID)
            Horse temp = horses[minIndex];
            horses[minIndex] = horses[i];
            horses[i] = temp;
        }
    }

    // Custom comparator horse IDs as integers
    private static int compareHorseIDs(String id1, String id2) {
        int intId1 = Integer.parseInt(id1);
        int intId2 = Integer.parseInt(id2);
        return Integer.compare(intId1, intId2);
    }
}

