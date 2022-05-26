package es.javimg.pilotosmotociclismo;

import es.javimg.pilotosmotociclismo.entities.Piloto;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Query;

public class PrimaryController implements Initializable {

    private Piloto pilotoSeleccionado;
    @FXML
    private TableView<Piloto> tableViewPilotos;
    @FXML
    private TableColumn<Piloto, String> columnNombre;
    @FXML
    private TableColumn<Piloto, String> columnApellidos;
    @FXML
    private TableColumn<Piloto, String> columnEstilo;
    @FXML
    private TableColumn<Piloto, String> columnEquipo;
    @FXML
    private TableColumn<Piloto, Integer> columnTitulos;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTitulos;
    @FXML
    private TextField textFieldBuscar;
    @FXML
    private Button ButtonBuscar;
    @FXML
    private CheckBox checkCoincide;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        //columnEstilo.setCellValueFactory(new PropertyValueFactory<>("estiloPilotaje"));
        columnEstilo.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    Piloto piloto = cellData.getValue();
                    if (piloto.getEstiloPilotaje() == 'P') {
                        String nombre = piloto.getEquipo().getNombre();
                        property.setValue("Preciso");
                    } else if (piloto.getEstiloPilotaje() == 'A'){
                        String nombre = piloto.getEquipo().getNombre();
                        property.setValue("Agresivo");
                    }
                    return property;
                });
        columnTitulos.setCellValueFactory(new PropertyValueFactory<>("numTitulos"));
        columnEquipo.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    Piloto piloto = cellData.getValue();
                    if (piloto.getEquipo() != null) {
                        String nombre = piloto.getEquipo().getNombre();
                        property.setValue(nombre);
                    }
                    return property;
                });
        tableViewPilotos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    pilotoSeleccionado = newValue;
                    if (pilotoSeleccionado != null){
                        textFieldNombre.setText(pilotoSeleccionado.getNombre());
                        textFieldApellidos.setText(pilotoSeleccionado.getApellidos());
                        textFieldTitulos.setText(String.valueOf(pilotoSeleccionado.getNumTitulos()));
                    }else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
        cargarTodosPiloto();
    }
    
    private void cargarTodosPiloto(){
        Query queryPilotoFindAll = App.em.createNamedQuery("Piloto.findAll");
        List<Piloto> listapiloto = queryPilotoFindAll.getResultList();
        System.out.print("a" + listapiloto.size());
        tableViewPilotos.setItems(FXCollections.observableArrayList(listapiloto));
    }
        
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (pilotoSeleccionado != null) {
            pilotoSeleccionado.setNombre(textFieldNombre.getText());
            pilotoSeleccionado.setApellidos(textFieldApellidos.getText());
            pilotoSeleccionado.setNumTitulos(Short.valueOf(textFieldTitulos.getText()));
            App.em.getTransaction().begin();
            App.em.merge(pilotoSeleccionado);
            App.em.getTransaction().commit();

            int numFilaSeleccionada = tableViewPilotos.getSelectionModel().getSelectedIndex();
            tableViewPilotos.getItems().set(numFilaSeleccionada,pilotoSeleccionado);
            TablePosition pos = new TablePosition(tableViewPilotos,numFilaSeleccionada, null);
            tableViewPilotos.getFocusModel().focus(pos);
            tableViewPilotos.requestFocus();
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        if(pilotoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(pilotoSeleccionado.getNombre() + " " 
                    + pilotoSeleccionado.getApellidos());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                App.em.getTransaction().begin();
                App.em.remove(pilotoSeleccionado);
                App.em.getTransaction().commit();
                tableViewPilotos.getItems().remove(pilotoSeleccionado);
                tableViewPilotos.getFocusModel().focus(null);
                tableViewPilotos.requestFocus();
            } else {
                int numFilaSeleccionada = tableViewPilotos.getSelectionModel().getSelectedIndex();
                tableViewPilotos.getItems().set(numFilaSeleccionada, pilotoSeleccionado);
                TablePosition pos = new TablePosition(tableViewPilotos, numFilaSeleccionada, null);
                tableViewPilotos.getFocusModel().focus(pos);
                tableViewPilotos.requestFocus();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try {
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            pilotoSeleccionado = new Piloto();
            secondaryController.setPiloto(pilotoSeleccionado);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            
        }
    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
    if(pilotoSeleccionado != null) {
        try {
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            secondaryController.setPiloto(pilotoSeleccionado);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Atencion");
        alert.setHeaderText("Debe seleccionar un registro");
        alert.showAndWait();
    }
    }

    @FXML
    private void onActionButtonBuscar(ActionEvent event) {
        
        if(!textFieldBuscar.getText().isEmpty()) {
            if(checkCoincide.isSelected()) {
                Query queryPilotoFindAll = App.em.createNamedQuery("Piloto.findByNombre");
                queryPilotoFindAll.setParameter("nombre", textFieldBuscar.getText());
                List<Piloto> listPiloto = queryPilotoFindAll.getResultList();
                tableViewPilotos.setItems(FXCollections.observableArrayList(listPiloto));
            } else {
                String strQuery = "SELECT * FROM Piloto WHERE LOWER(nombre) LIKE ";
                strQuery += "\'%" + textFieldBuscar.getText().toLowerCase() + "%\'";
                Query queryPilotoFindAll = App.em.createNativeQuery(strQuery, Piloto.class);
                
                List<Piloto> listPiloto = queryPilotoFindAll.getResultList();
                tableViewPilotos.setItems(FXCollections.observableArrayList(listPiloto));
                
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, strQuery);
            }
        } else {
            cargarTodosPiloto();
        }
    }
    
 
}
        
            
        

    
    
