module com.securefilevault {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.apache.commons.lang3;

    opens com.securefilevault to javafx.fxml;
    opens com.securefilevault.controllers to javafx.fxml;
    exports com.securefilevault;
    exports com.securefilevault.controllers;
} 