module ua.nekotov.mpui.mpui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.json;
    requires java.desktop;

    opens ua.nekotov.mpui.mpui to javafx.fxml;
    exports ua.nekotov.mpui.mpui;
}