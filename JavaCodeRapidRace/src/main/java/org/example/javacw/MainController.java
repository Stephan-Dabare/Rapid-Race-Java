package org.example.javacw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    public Button btnUpdate;
    @FXML
    public Label updateLabel;
    @FXML
    public Button btnUploadImage;
    @FXML
    public Button btnSaveFile;
    @FXML
    public Button btnOpenText;
    @FXML
    public Button btnRaceStart;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    public TextField textId;
    @FXML
    public TextField textName;
    @FXML
    public TextField textJockey;
    @FXML
    public TextField textAge;
    @FXML
    public TextField textBreed;
    @FXML
    public TextField textCompete;
    @FXML
    public TextField textWin;
    @FXML
    public TextField textGroup;
    @FXML
    public CheckBox checkImage;


    // Table columns
    @FXML
    public TableColumn<Horse, String> colName;
    @FXML
    public TableColumn<Horse, String> colJockey;
    @FXML
    public TableColumn<Horse, Integer> colAge;
    @FXML
    public TableColumn<Horse, String> colBreed;
    @FXML
    public TableColumn<Horse, Integer> colRecords;
    @FXML
    public TableColumn<Horse, String> colGroup;
    @FXML
    public Button btnSubmit;
    @FXML
    public Button btnRemove;
    @FXML
    public Button btnView;

    @FXML
    private TableView<Horse> tableHorseDetails;

    @FXML
    private TableColumn<Horse, String> colId;

    private DataModel dataModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        dataModel = DataModel.getInstance();
        // Set the table columns to the data model's list of horses.
        tableHorseDetails.setItems(dataModel.getHorses());
        colId.setCellValueFactory(new PropertyValueFactory<Horse, String>("horseID"));
        colName.setCellValueFactory(new PropertyValueFactory<Horse, String>("horseName"));
        colJockey.setCellValueFactory(new PropertyValueFactory<Horse, String>("jockeyName"));
        colAge.setCellValueFactory(new PropertyValueFactory<Horse, Integer>("age"));
        colRecords.setCellValueFactory(new PropertyValueFactory<Horse, Integer>("wins"));
        colBreed.setCellValueFactory(new PropertyValueFactory<Horse, String>("breed"));
        colGroup.setCellValueFactory(new PropertyValueFactory<Horse, String>("group"));
    }

    // Method to clear the entered text fields.
    @FXML
    void clearFields() {
        textId.clear();
        textName.clear();
        textJockey.clear();
        textAge.clear();
        textBreed.clear();
        textCompete.clear();
        textWin.clear();
        textGroup.clear();
        checkImage.setSelected(false); // Clear the checkbox selection
    }

    // Submit button method.
    @FXML
    void submit(ActionEvent event) {
        String enteredID = textId.getText().trim();

        // Check if the ID is empty.
        if (enteredID.isEmpty()) {
            showAlert("Horse ID cannot be empty.");
            return;
        }

        // Check if the entered ID is a positive integer.
        try {
            int id = Integer.parseInt(enteredID);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert("Horse ID must be a positive integer.");
            return;
        }

        // Check if the ID is already taken.
        if (dataModel.isIDTaken(enteredID)) {
            // If the update operation is in progress, proceed with updating horse details.
            if (updateLabel.isVisible()) {
                updateHorse1(event, enteredID);
            } else {
                showAlert("Horse ID is already taken.");
            }
            return;
        }

        // If the update button is not pressed and ID is not taken, add a new horse.
        if (!updateLabel.isVisible()) {
            addNewHorse();
        }
    }

    // Method to add new horses.
    private void addNewHorse() {
        // Checks for group availability.
        String group = textGroup.getText().toUpperCase();
        if (!dataModel.registerHorseForGroup(group)) {
            showAlert("All slots filled for group " + group);
            return;
        }
        try {
            // Get age from text field.
            int age = Integer.parseInt(textAge.getText());
            // Get wins from text field.
            int wins = Integer.parseInt(textWin.getText());
            // Get compete from text field.
            int compete = Integer.parseInt(textCompete.getText());
            // Get breed from text field.
            String breed = textBreed.getText();

            // Create a new Horse object with all arguments.
            Horse horse = new Horse(textId.getText(), textName.getText(), textJockey.getText(),
                    age, wins, compete, breed, group);

            dataModel.getHorses().add(horse);
            // If submission is successful, clear the fields.
            clearFields();
        } catch (NumberFormatException e) {
            // Check for input validation.
            showAlert("Please enter valid numbers for age, wins, and compete.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }


    // Method to update horse details
    private void updateHorse1(ActionEvent event, String enteredID) {
        Horse existingHorse = dataModel.getHorseByID(enteredID);
        if (existingHorse != null) {
            // Decrement the count of the old group and update group if needed.
            String oldGroup = existingHorse.getGroup();
            String newGroup = textGroup.getText().toUpperCase();
            if (!oldGroup.equals(newGroup)) {
                if (!dataModel.updateHorseGroup(enteredID, newGroup)) {
                    showAlert("Failed to update horse group. Please try again.");
                    return;
                }
            }
            try {
                // Update horse details
                existingHorse.setHorseName(textName.getText());
                existingHorse.setJockeyName(textJockey.getText());
                existingHorse.setAge(Integer.parseInt(textAge.getText()));
                existingHorse.setBreed(textBreed.getText());
                existingHorse.setCompete(Integer.parseInt(textCompete.getText()));
                existingHorse.setWins(Integer.parseInt(textWin.getText()));
                existingHorse.setGroup(newGroup);
                tableHorseDetails.refresh(); // Refresh the table view to reflect changes.
                // If submission is successful, clear the fields.
                clearFields();
                // Hide the update notification label
                updateLabel.setVisible(false);
            } catch (NumberFormatException e) {
                showAlert("Please enter valid numbers for age, wins, and compete.");
            } catch (IllegalArgumentException e) {
                showAlert(e.getMessage());
            }
        }
    }


    // Method to show alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method that assign to delete horse details button.
    @FXML
    void removeHorse(ActionEvent event) {
        Horse selectedHorse = tableHorseDetails.getSelectionModel().getSelectedItem();
        if (selectedHorse != null) {
            // Decrement the count of the group to which the horse belongs.
            String group = selectedHorse.getGroup();
            if (dataModel.decrementGroupCount(group)) {
                // Remove the horse from the list.
                dataModel.getHorses().remove(selectedHorse);
                tableHorseDetails.getItems().remove(selectedHorse);
            } else {
                // If the group count couldn't be decremented, show an error message
                showAlert("Failed to decrement group count. Please try again.");
            }
        }
    }

    // Method to view horse details.
    @FXML
    void goToViewDetails(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-horse.fxml"));
        Parent root = loader.load();
        ViewHorseController controller = loader.getController();

        // Pass the DataModel instance to the ViewHorseController
        controller.initData(DataModel.getInstance());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method to update horse details.
    @FXML
    void updateHorse(ActionEvent event) {
        String enteredID = textId.getText();
        if (dataModel.isIDTaken(enteredID)) {
            updateLabel.setVisible(true);
            // Populate text fields with horse details
            Horse existingHorse = dataModel.getHorseByID(enteredID);
            if (existingHorse != null) {
                textName.setText(existingHorse.getHorseName());
                textJockey.setText(existingHorse.getJockeyName());
                textAge.setText(String.valueOf(existingHorse.getAge()));
                textBreed.setText(existingHorse.getBreed());
                textCompete.setText(String.valueOf(existingHorse.getCompete()));
                textWin.setText(String.valueOf(existingHorse.getWins()));
                textGroup.setText(existingHorse.getGroup());
            }
        } else {
            updateLabel.setVisible(false);
            showAlert("Horse ID is not found.");
        }
    }

    // Method to upload an image.
    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(btnUploadImage.getScene().getWindow());
        if (selectedFile != null) {
            openFile(selectedFile);
        }
    }

    // Method to save horse details to text file.
    @FXML
    void saveToFile(ActionEvent event) {
        // Check if the DataModel instance is initialized
        if (dataModel != null) {
            // Call the saveToFile method from the DataModel instance
            dataModel.saveToFile("horse_data.txt");
            // Show a confirmation message to the user
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File Saved");
            alert.setHeaderText(null);
            alert.setContentText("Horse details saved to file.");
            alert.showAndWait();
        } else {
            // If DataModel is not initialized, show an error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("DataModel is not initialized.");
            alert.showAndWait();
        }
    }

    // Method to open text file, assign to open text file button.
    @FXML
    void openTextFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File selectedFile = fileChooser.showOpenDialog(btnOpenText.getScene().getWindow());
        if (selectedFile != null) {
            openFile(selectedFile);
        }
    }

    // Open window to open the text file.
    private void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                // Open the file using default system application.
                Desktop.getDesktop().open(file);
            } else {
                throw new UnsupportedOperationException("Desktop is not supported.");
            }
        } catch (IOException ex) {
            showAlert("Failed to open the file.");
            ex.printStackTrace();
        }
    }

    // Method to go to horse race view. Assign to start race button.
    @FXML
    void goToHorseRace(ActionEvent event) throws IOException {
        // Check if each group has at least one horse.
        if (!dataModel.isGroupComplete()) {
            // Show an error message if any group is incomplete.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Each group (A, B, C, D) must have at least one horse.");
            alert.showAndWait();
            return;
        }

        // Save data to a text file.
        dataModel.saveToFile("horse_data.txt");

        // Proceed to the horse race view if all groups are complete and data is saved.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("horse-race-view.fxml"));
        Parent root = loader.load();
        HorseRaceController controller = loader.getController();

        // Pass the DataModel instance to the HorseRaceController.
        controller.initData(DataModel.getInstance());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}