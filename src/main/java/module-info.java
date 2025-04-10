module com.example.vietnamtravel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    // Add this line
    requires org.hibernate.orm.core;
    requires java.xml.bind;
    requires java.desktop;

    opens model to org.hibernate.orm.core;

    opens com.example.vietnamtravel to javafx.fxml;
    exports com.example.vietnamtravel;

    opens controller to javafx.fxml, java.xml.bind;
    exports controller;

}