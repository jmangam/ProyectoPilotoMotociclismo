module es.javimg.pilotosmotociclismo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.instrument;
    requires java.persistence;
    requires java.sql;
    requires java.base;

    opens es.javimg.pilotosmotociclismo.entities;
    opens es.javimg.pilotosmotociclismo to javafx.fxml;
    exports es.javimg.pilotosmotociclismo;
}
