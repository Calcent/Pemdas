module com.example.pemdas_calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;


    opens com.example.pemdas_calculator to javafx.fxml;
    exports com.example.pemdas_calculator;
}