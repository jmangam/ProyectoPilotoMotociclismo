package es.javimg.pilotosmotociclismo;

import es.javimg.pilotosmotociclismo.entities.Equipo;
import es.javimg.pilotosmotociclismo.entities.Piloto;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.Query;
import javax.persistence.RollbackException;

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
    private ComboBox<Equipo> comboBoxEquipo;
    @FXML
    private RadioButton radioButtonAgresivo;
    @FXML
    private ToggleGroup groupEstilo;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private RadioButton radioButtonPreciso;
    @FXML
    private BorderPane rootSecondary;

    @Override
    public void initialize(URL url, ResourceBundle rb) {    
    }
   
    public void setPiloto(Piloto piloto) {
        App.em.getTransaction().begin();
        this.piloto = piloto;
        mostrarDatos();
    }

    private void mostrarDatos() {
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
            LocalDate localDate = zdt.toLocalDate();
            dateNacimiento.setValue(localDate);
        }

        Query queryEquipoFindAll = App.em.createNamedQuery("Equipo.findAll");
        List<Equipo> listEquipo = queryEquipoFindAll.getResultList();
        System.out.println(listEquipo.size());
        comboBoxEquipo.setItems(FXCollections.observableList(listEquipo));

        if (piloto.getEquipo() != null) {
            comboBoxEquipo.setValue(piloto.getEquipo());
        }
        
        comboBoxEquipo.setCellFactory((ListView<Equipo> l) -> new ListCell<Equipo>() {
            @Override
            protected void updateItem(Equipo equipo, boolean empty) {
                super.updateItem(equipo, empty);
                if (equipo == null || empty) {
                    setText("");
                } else {
                    setText(equipo.getNombre());
                }
            }
        });  
        
        comboBoxEquipo.setConverter(new StringConverter<Equipo>() {
            @Override
            public String toString(Equipo provincia) {
                if (provincia == null) {
                    return null;
                } else {
                    return provincia.getNombre();
                }
            }
            @Override
            public Equipo fromString(String userId) {
                return null;
            }          
        });
        
        if (piloto.getFoto() != null) {
            String imageFileName = piloto.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()){
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imagen");
                alert.showAndWait();
            }
        }
    }
                    
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        int numFilaSeleccionada;
        boolean errorFormato = false;
        
        piloto.setNombre(textfieldNombre.getText());
        piloto.setApellidos(textFieldApellidos.getText());
        piloto.setEmail(textFieldEmail.getText());
                
        if (textFieldTitulos.getText().isEmpty()) {
            try {
                piloto.setNumTitulos(Short.valueOf(textFieldTitulos.getText()));    
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Numero de titulos no valido");
                alert.showAndWait();
                textFieldTitulos.requestFocus();
            }
        }
        
        if (!textFieldSalario.getText().isEmpty()) {
            try {
                    piloto.setSalario(BigDecimal.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()));
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert (Alert.AlertType.INFORMATION, "Salario no valido");
                alert.showAndWait();
                textFieldSalario.requestFocus();
            }
        }
        
        piloto.setCampeon(checkboxCampeon.isSelected());
        
        if (radioButtonAgresivo.isSelected()) {
            piloto.setEstiloPilotaje(AGRESIVO);
        } else if(radioButtonPreciso.isSelected()) {
            piloto.setEstiloPilotaje(PRECISO);
        }
        
        if(dateNacimiento.getValue() != null) {
            LocalDate localDate = dateNacimiento.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            piloto.setFechaNacimiento(date);
        } else {
            piloto.setFechaNacimiento(null);
        }
        
        piloto.setEquipo(comboBoxEquipo.getValue());
        
        if (!errorFormato) {
            try {
                if (piloto.getId() == null) {
                    System.out.println("Guardando nuevo piloto en BD");
                    App.em.persist(piloto);
                } else {
                    System.out.println("Actualizando piloto en BD");
                    App.em.merge(piloto);
                }
                App.em.getTransaction().commit();
                
                App.setRoot("primary");
            } catch (RollbackException ex) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        App.em.getTransaction().rollback();
        try {
            App.setRoot("primary");
        } catch (IOException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists()) {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagenes(jpg, png)", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        if(file != null) {
            try {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                piloto.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }
    

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresion de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el achivo asociado a la imagen, \n"
                +	"quitar la foto pero MANTENER el archivo, \no CANCELAR la operacion?");
        alert.setContentText("Elija la opcion deseada:");

        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar) {
                String imageFileName = piloto.getFoto();
                File file = new File(CARPETA_FOTOS + "/" + imageFileName);
                if (file.exists()) {
                        file.delete();
                }
                piloto.setFoto(null);
                imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener) {
                piloto.setFoto(null);
                imageViewFoto.setImage(null);
        }
    }
    
        
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

}