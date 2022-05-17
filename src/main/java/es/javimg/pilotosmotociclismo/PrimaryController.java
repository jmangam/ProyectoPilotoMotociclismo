package es.javimg.pilotosmotociclismo;

import es.javimg.pilotosmotociclismo.entities.Piloto;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
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
   
    }
    
    
    
