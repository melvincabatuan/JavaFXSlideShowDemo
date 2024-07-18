module com.example.slideshowdemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.slideshowdemo to javafx.fxml;
    exports com.example.slideshowdemo;
}