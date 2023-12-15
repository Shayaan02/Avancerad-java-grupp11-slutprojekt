module com.example.inlm3n {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inlm3n to javafx.fxml;
    exports com.example.inlm3n;
}