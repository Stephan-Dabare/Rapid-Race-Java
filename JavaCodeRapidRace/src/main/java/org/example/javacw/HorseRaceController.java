package org.example.javacw;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class HorseRaceController implements Initializable {
    @FXML
    public Button btnBackToMain;
    @FXML
    public Button btnSelectHorse;
    @FXML
    public BarChart<String, Number> raceTimeChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private TableView<Horse> tableView;
    @FXML
    private TableColumn<Horse, String> colId;
    @FXML
    private TableColumn<Horse, String> colName;
    @FXML
    private TableColumn<Horse, Integer> colAge;
    @FXML
    private TableColumn<Horse, Integer> colWins;
    @FXML
    private TableColumn<Horse, String> colBreed;
    @FXML
    private TableColumn<Horse, String> colJockey;
    @FXML
    private TableColumn<Horse, String> colGroup;
    @FXML
    public Button btnRaceBegin;
    @FXML
    private TableView<Horse> tableViewWinHorse;
    @FXML
    public TableColumn<Horse, Integer> colPlace;
    @FXML
    public TableColumn<Horse, Double> colTime;
    @FXML
    private TableColumn<Horse, String> colWinId;
    @FXML
    private TableColumn<Horse, String> colWinName;
    @FXML
    private TableColumn<Horse, String> colWinJockey;
    @FXML
    private TableColumn<Horse, String> colWinBreed;
    @FXML
    private TableColumn<Horse, Integer> colWinAge;
    @FXML
    private TableColumn<Horse, String> colWinGroup;

    private DataModel dataModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns for selected horses for finals.
        colId.setCellValueFactory(new PropertyValueFactory<>("horseID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        colJockey.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));

        // Initialize table columns for race results.
        colWinId.setCellValueFactory(new PropertyValueFactory<>("horseID"));
        colWinName.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        colWinJockey.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        colWinBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        colWinAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colWinGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("raceTime"));

        // Set labels for X and Y axes of the bar chart.
        xAxis.setLabel("Horse");
        yAxis.setLabel("Time (seconds)");

    }

    // Method to go back to main menu. Assign to back button.
    @FXML
    void goToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void initData(DataModel instance) {
        // Set the dataModel field to the provided current instance.
        this.dataModel = instance;
    }


    // Method to select 4 random horses and populate the table.
    // Assign to select horse for finals button.
    @FXML
    void selectHorsesRace(ActionEvent event) {
        Map<String, Horse> randomHorses = dataModel.selectRandomHorses();
        Random random = new Random();
        // Assign random race times to each horse.
        randomHorses.values().forEach(horse -> horse.setRaceTime(random.nextInt(91))); // Assign random race time between 0 to 90 seconds
        // Display the randomly selected horses in the Table.
        if (!randomHorses.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList(randomHorses.values()));
        } else {
            // Handle no horses are available error.
            System.out.println("No horses available for the race.");
        }
    }

    // Method to start thr race.
    // Assign to start race button.
    @FXML
    void raceBegin(ActionEvent event) {
        if (tableView.getItems().isEmpty()) {
            // Show message if horses are not selected for finals yet.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Horses not selected for finals yet!");
            alert.showAndWait();
            return;
        }
        // Sort the horses based on their race times.
        Horse[] horsesArray = tableView.getItems().toArray(new Horse[0]);
        CustomSort.selectionSort(horsesArray);
        // Display the sorted horses with time.
        displayRaceResults(horsesArray);
        // Visualize the race times using the bar chart.
        visualizeRaceTimes(horsesArray);
    }

    // Method to display race results in a table.
    private void displayRaceResults(Horse[] sortedHorses) {
        // Clear the table before populating it with new results
        tableViewWinHorse.getItems().clear();

        // Add the first 3 horses to the winning horses table
        for (int i = 0; i < Math.min(3, sortedHorses.length); i++) {
            sortedHorses[i].setPlace(i + 1);
            tableViewWinHorse.getItems().add(sortedHorses[i]);
        }
    }

    // Method to visualize horse results in a bar chart.
    private void visualizeRaceTimes(Horse[] sortedHorses) {
        raceTimeChart.getData().clear(); // Clear previous data.

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < Math.min(3, sortedHorses.length); i++) {
            Horse horse = sortedHorses[i];
            series.getData().add(new XYChart.Data<>(horse.getHorseName(), horse.getRaceTime()));
        }

        raceTimeChart.getData().add(series);
    }
}
