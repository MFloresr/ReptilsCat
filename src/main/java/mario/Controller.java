package mario;


import com.mysql.jdbc.Connection;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Controller {
    @FXML
    private TextField textnom;
    @FXML
    private TextField textespecie;
    @FXML
    private TextArea textdescripcio;
    @FXML
    private ImageView imagen;
    @FXML
    private ComboBox comboestat;
    @FXML
    private ComboBox comboordre;
    @FXML
    private ChoiceBox choisefamilia;
    @FXML
    private Button btnanterior;
    @FXML
    private Button btnsiguiente;
    @FXML
    private Button btnguardarcambios;

    private java.sql.Connection con = null;
    private ResultSet resultat;

    @FXML
    public void initialize() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cendrassos", "mario", "qcmmer2sa!");
            PreparedStatement peticionfamilia = con.prepareStatement("SELECT * FROM families");
            resultat = peticionfamilia.executeQuery();
            while(resultat.next()){
                System.out.println(resultat.getInt("codi")+"_"+ resultat.getString("nom"));
                cargarChoise(resultat);
            }
            PreparedStatement peticionordre = con.prepareStatement("SELECT * FROM ordres WHERE familia=?");
            peticionordre.setInt(1,1);
            resultat = peticionordre.executeQuery();
            while(resultat.next()){
                System.out.println(resultat.getInt("familia")+"_"+ resultat.getString("nom"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cargarChoise(ResultSet resultat){
        try {
            choisefamilia.getItems().add(resultat.getString("nom"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        choisefamilia.getSelectionModel().select(0);
    }

    @FXML
    public void guardarCambios(Event event){

    }

    @FXML
    public void buscarAnterior(Event event){

    }

    @FXML
    public void buscarSiguiente(Event event){

    }

}
