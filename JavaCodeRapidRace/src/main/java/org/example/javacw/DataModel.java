package org.example.javacw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataModel {
    private static final DataModel instance = new DataModel();
    private ObservableList<Horse> horses = FXCollections.observableArrayList();
    private Map<String, Integer> groupCount = new HashMap<>();

    public DataModel() {
        // Initialize group counts.
        groupCount.put("A", 0);
        groupCount.put("B", 0);
        groupCount.put("C", 0);
        groupCount.put("D", 0);
    }

    public static DataModel getInstance() {
        return instance;
    }

    public ObservableList<Horse> getHorses() {
        return horses;
    }

    // Method to check horse ID is already taken.
    public boolean isIDTaken(String horseID) {
        for (Horse horse : horses) {
            if (horse.getHorseID().equals(horseID)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> getGroupCount() {
        return groupCount;
    }

    // Method to access horse info based on ID.
    public Horse getHorseByID(String horseID) {
        for (Horse horse : horses) {
            if (horse.getHorseID().equals(horseID)) {
                return horse;
            }
        }
        return null;
    }

    // Method to add horse for a group. It checks the limit (<5).
    public boolean registerHorseForGroup(String group) {
        if (groupCount.containsKey(group)) {
            int count = groupCount.get(group);
            if (count < 5) {
                groupCount.put(group, count + 1);
                return true;
            } else {
                return false; // When Limit exceeded of selected group.
            }
        }
        return false; // Invalid group.
    }

    public boolean decrementGroupCount(String group) {
        if (groupCount.containsKey(group)) {
            int count = groupCount.get(group);
            if (count > 0) {
                groupCount.put(group, count - 1);
                return true; // Group count decremented successfully.
            } else {
                return false; // Group count is already zero.
            }
        }
        return false; // Return false when enter Invalid group.
    }


    public boolean updateHorseGroup(String horseID, String newGroup) {
        Horse horse = getHorseByID(horseID);
        if (horse != null) {
            String oldGroup = horse.getGroup();
            // Check if the new group same as the old group.
            if (oldGroup.equals(newGroup)) {
                return true; // same group, Group not changed.
            }
            // Decrement count of old group.
            if (decrementGroupCount(oldGroup)) {
                // Increment count of new group.
                if (registerHorseForGroup(newGroup)) {
                    // Update horse's new group.
                    horse.setGroup(newGroup);
                    return true; // updated successfully
                } else {
                    // New group limit exceeded, come back to count of old group.
                    registerHorseForGroup(oldGroup);
                }
            }
        }
        return false; // Failed to change the group.
    }

    public boolean isGroupComplete() {
        for (int count : groupCount.values()) {
            if (count == 0) {
                return false; // Even one group is incomplete
            }
        }
        return true; // If all groups have at least one horse.
    }


    // Method to save details to a text file.
    public void saveToFile(String filename) {
        // Open text file to write.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String group : groupCount.keySet()) {
                // Add details base on horse groups.
                writer.write("Group " + group + ":\n");
                for (Horse horse : horses) {
                    if (horse.getGroup().equals(group)) {
                        // Add horse details to text file.
                        writer.write(horse.toString() + "\n");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method select random horses.
    public Map<String, Horse> selectRandomHorses() {
        Map<String, Horse> randomHorses = new HashMap<>();
        Random random = new Random();
        for (String group : groupCount.keySet()) {
            List<Horse> horsesInGroup = new ArrayList<>();
            // Collect horses in the current group.
            for (Horse horse : horses) {
                if (horse.getGroup().equals(group)) {
                    horsesInGroup.add(horse);
                }
            }
            // Randomly select a horse from each group.
            if (!horsesInGroup.isEmpty()) {
                int randomIndex = random.nextInt(horsesInGroup.size());
                randomHorses.put(group, horsesInGroup.get(randomIndex));
            }
        }
        return randomHorses;
    }


}
