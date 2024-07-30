module org.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.javacw to javafx.fxml;
    exports org.example.javacw;
}