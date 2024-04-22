module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;
    requires javafx.swing;
    
    
    

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
    
}
