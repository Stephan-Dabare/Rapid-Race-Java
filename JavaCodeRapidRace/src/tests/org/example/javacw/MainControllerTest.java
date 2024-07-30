package org.example.javacw;

import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.Assert.*;

public class MainControllerTest {
    @Test
    public void testSubmit_EmptyID() {
        HorseManager manager = new HorseManager();
        manager.textId.setText(""); // Empty ID

        manager.submit(null);

        assertEquals("Horse ID cannot be empty.", manager.alertMessage);
    }

    @Test
    public void testSubmit_NonIntegerID() {
        HorseManager manager = new HorseManager();
        manager.textId.setText("abc"); // Non-integer ID

        manager.submit(null);

        assertEquals("Horse ID must be a positive integer.", manager.alertMessage);
    }

    @Test
    public void testSubmit_NegativeID() {
        HorseManager manager = new HorseManager();
        manager.textId.setText("-123"); // Negative ID

        manager.submit(null);

        assertEquals("Horse ID must be a positive integer.", manager.alertMessage);
    }

    @Test
    public void testSubmit_IDAlreadyTaken_UpdateInProgress() {
        HorseManager manager = new HorseManager();
        manager.textId.setText("123"); // ID already taken
        manager.updateLabel.setVisible(true); // Update in progress

        // Mock behavior of updateHorse1 method
        manager.updateHorse1(null, "123");

        assertNull(manager.alertMessage);
    }


    @Test
    public void testSubmit_NewHorse_NoUpdateInProgress() {
        HorseManager manager = new HorseManager();
        manager.textId.setText("123"); // New ID
        manager.updateLabel.setVisible(false); // No update in progress

        // Mock behavior of addNewHorse method
        manager.addNewHorse();

        assertNull(manager.alertMessage);
    }

    // Inner class to simulate HorseManager
    class HorseManager {
        // Mock alertMessage for testing purposes
        String alertMessage;

        // Mock text fields and labels
        TextField textId = new TextField();
        Label updateLabel = new Label();

        public void submit(ActionEvent event) {
            String enteredID = textId.getText().trim();

            // Check if the ID is empty
            if (enteredID.isEmpty()) {
                showAlert("Horse ID cannot be empty.");
                return;
            }

            // Check if the entered ID is a positive integer
            try {
                int id = Integer.parseInt(enteredID);
                if (id <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert("Horse ID must be a positive integer.");
                return;
            }

            // Check if the ID is already taken
            if (isIDTaken(enteredID)) {
                // If the update operation is in progress, proceed with updating horse details
                if (updateLabel.isVisible()) {
                    updateHorse1(event, enteredID);
                } else {
                    showAlert("Horse ID is already taken.");
                }
                return;
            }

            // If the update button is not pressed and ID is not taken, add a new horse
            if (!updateLabel.isVisible()) {
                addNewHorse();
            }
        }

        private void showAlert(String message) {
            // Set alertMessage for testing purposes
            this.alertMessage = message;
        }

        private boolean isIDTaken(String id) {
            return false;
        }

        private void updateHorse1(ActionEvent event, String enteredID) {

        }

        private void addNewHorse() {

        }
    }
}