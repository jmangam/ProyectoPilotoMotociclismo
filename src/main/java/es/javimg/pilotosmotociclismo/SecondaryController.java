package es.javimg.pilotosmotociclismo;

import es.javimg.pilotosmotociclismo.entities.Piloto;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javax.persistence.Query;

public class SecondaryController  implements Initializable {
    
    private Piloto piloto;
    private boolean nuevoPiloto;
    
    
    private static final char AGRESIVO = 'A';
    private static final char PRECISO = 'P';
    
    private static final String CARPETA_FOTOS = "Fotos";

    @FXML
    private TextField textfieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldTitulos;
    @FXML
    private DatePicker dateNacimiento;
    @FXML
    private CheckBox checkboxCampeon;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private ChoiceBox<?> comboBoxEquipo;
    @FXML
    private RadioButton radioButtonAgresivo;
    @FXML
    private ToggleGroup groupEstilo;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private RadioButton radioButtonPreciso;

    @Override
    public void initialize(URL url, ResourceBundle rb) {    
    }
   
    public void setPiloto(Piloto piloto) {
    App.em.getTransaction().begin();
    this.piloto = piloto;
    mostrarDatos();
    }

    private void MostrarDatos() {
        textfieldNombre.setText(piloto.getNombre());
        textFieldApellidos.setText(piloto.getApellidos());
        textFieldEmail.setText(piloto.getEmail());
        textfieldNombre.setText(piloto.getNombre());
        
        if (piloto.getNumTitulos() != null ) {
            textFieldTitulos.setText(String.valueOf(piloto.getNumTitulos()));
        }
        
        if (piloto.getSalario() != null ) {
            textFieldSalario.setText(String.valueOf(piloto.getSalario()));  
        }
        
        if (piloto.getCampeon() != null ) {
            checkboxCampeon.setSelected(piloto.getCampeon());  
        }
        
        if (piloto.getEstiloPilotaje() != null) {
            switch (piloto.getEstiloPilotaje()) {
                case AGRESIVO:
                    radioButtonAgresivo.setSelected(true);
                    break;
                case PRECISO:
                    radioButtonPreciso.setSelected(true);
                    break;
            }
        }
        
        if (piloto.getFechaNacimiento() != null) {
            Date date = piloto.getFechaNacimiento();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.tolLocalDate();
            datePickerFechaNacimiento.setValuel(localDate);
        }

Query queryProvinciaFindAll = App.em.createNamedQuery("Provincia.findAl1");
List<Provincia> listProvincia = queryProvinciaFindAll.getResultList();

comboBoxProvincia.setItems(FXCollections.observableList(listProvincia));

if (persona.getProvincia() != null) 4
comboBoxProvincia.setValue(persona.getProvincia());
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}