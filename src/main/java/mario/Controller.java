package mario;


import com.mysql.jdbc.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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
    private ObservableList<String> itemsOrdre = FXCollections.observableArrayList ();
    private ArrayList<Animal> animales = new ArrayList<Animal>();

    @FXML
    public void initialize() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cendrassos", "mario", "qcmmer2sa!");
            PreparedStatement peticionfamilia = con.prepareStatement("SELECT * FROM families");
            resultat = peticionfamilia.executeQuery();
            //int familia = resultat.getInt("codi");
            //System.out.println(familia);
            while(resultat.next()){
                System.out.println(resultat.getInt("codi")+"_"+ resultat.getString("nom"));
                cargarChoise(resultat);
            }
            PreparedStatement peticionordre = con.prepareStatement("SELECT * FROM ordres WHERE familia=?");
            peticionordre.setInt(1,1);
            resultat = peticionordre.executeQuery();
            while(resultat.next()){
                System.out.println(resultat.getInt("familia")+"_"+ resultat.getString("nom"));
                itemsOrdre.add(resultat.getString("nom"));
            }
            comboordre.setItems(itemsOrdre);
            comboordre.getSelectionModel().select(0);
            /*for (int i =0;i<itemsOrdre.size();i++){
                System.out.println(itemsOrdre.get(i));
            }*/


            PreparedStatement peticionanimals = con.prepareStatement("SELECT * FROM animals WHERE ordre=?");
            peticionanimals.setInt(1,1);
            resultat = peticionanimals.executeQuery();
            while(resultat.next()){
                Animal animal = new Animal(resultat.getInt("codi"),resultat.getString("nom"),resultat.getInt("ordre"),resultat.getString("especie"),resultat.getString("descripcio"),resultat.getString("estat"),resultat.getString("imatge"));
                animales.add(animal);
                //itemsOrdre.add(resultat.getString("nom"));
            }
            textnom.setText(animales.get(0).getNom());
            textdescripcio.setText(animales.get(0).getDescripcio());
            textdescripcio.setWrapText(true);
            textespecie.setText(animales.get(0).getEspecie());
            String url = animales.get(0).getImatge();
            System.out.println(url);
            Image image= new Image("http://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Salamandra_salamandra_MHNT_1.jpg/130px-Salamandra_salamandra_MHNT_1.jpg");
            imagen.setImage(image);

            for (int i =0;i<animales.size();i++){
                System.out.println(animales.get(i));
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
