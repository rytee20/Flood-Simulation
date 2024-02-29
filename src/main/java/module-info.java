module com.example.deluge3d {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.deluge3d to javafx.fxml;
    exports com.example.deluge3d;
}