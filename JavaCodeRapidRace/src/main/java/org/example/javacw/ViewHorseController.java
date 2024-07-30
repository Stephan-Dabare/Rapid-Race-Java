package org.example.javacw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewHorseController implements Initializable {
    @FXML
    public Button backToMain;
    @FXML
    public ImageView imageView;
    @FXML
    public TableColumn<Horse, Image> colImg;
    @FXML
    public Button btnSort;
    @FXML
    private TableView<Horse> tableView;
    @FXML
    private TableColumn<Horse, String> colId;
    @FXML
    private TableColumn<Horse, String> colName;
    @FXML
    private TableColumn<Horse, String> colJockey;
    @FXML
    private TableColumn<Horse, Integer> colAge;
    @FXML
    private TableColumn<Horse, String> colBreed;
    @FXML
    public TableColumn<Horse, Integer> colRecord;
    @FXML
    private TableColumn<Horse, String> colGroup;

    private List<Horse> horses;

    private DataModel dataModel;

    public void initData(DataModel dataModel) {
        this.dataModel = dataModel;
        updateTable();
    }

    // Method to refresh the table.
    private void updateTable() {
        if (horses != null) {
            ObservableList<Horse> horseList = FXCollections.observableArrayList(horses);
            tableView.setItems(horseList);
        }
    }

    // Initialize table columns for view horse details.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataModel = DataModel.getInstance();
        // Set the table items to the data model's list of horses
        tableView.setItems(dataModel.getHorses());
        colId.setCellValueFactory(new PropertyValueFactory<>("horseID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        colJockey.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        colRecord.setCellValueFactory(new PropertyValueFactory<>("wins"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
    }

    // Method to go back to the main page.
    @FXML
    void goToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method created to assign to 'sort table' button.
    @FXML
    void sortTable(ActionEvent event) {
        // Get the list of horses.
        List<Horse> horseList = dataModel.getHorses();

        // Convert the list to an array.
        Horse[] horsesArray = horseList.toArray(new Horse[0]);

        // Sort the array using custom selection sort.
        CustomSortView.selectionSort(horsesArray);

        // Clear the table and add the sorted horses back.
        tableView.getItems().clear();
        tableView.getItems().addAll(horsesArray);
    }

}
