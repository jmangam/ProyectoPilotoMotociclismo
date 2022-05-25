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
import javafx.scene.control.ButtonType;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEstilo.setCellValueFactory(new PropertyValueFactory<>("estiloPilotaje"));
        columnTitulos.setCellValueFactory(new PropertyValueFactory<>("numTitulos"));
        columnEquipo.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getEquipo() != null) {
                        String nombre = cellData.getValue().getEquipo().getNombre();
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
                    }else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
        cargarTodosPiloto();
    }
    
    private void cargarTodosPiloto(){
        Query queryEstadioFindAll = App.em.createNamedQuery("Piloto.findAll");
        List<Piloto> listapiloto = queryEstadioFindAll.getResultList();
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
    }
    
 
}
        
            
        

    
    
