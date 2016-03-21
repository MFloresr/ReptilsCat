package mario;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ObservableList<String> itemsEstat = FXCollections.observableArrayList ();
    private ArrayList<Animal> animales = new ArrayList<Animal>();
    private int numposicio=0;

    @FXML
    public void initialize() {
        try {
            abrirConexion();
            familias();
            ordre();
            ordreAnimals();
            datosAnimales();
            estadoAnimal();
            cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirConexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cendrassos", "mario", "qcmmer2sa!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cerrarConexion(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void familias() throws SQLException {
        PreparedStatement peticionfamilia = null;
        try {
            peticionfamilia = con.prepareStatement("SELECT * FROM families");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultat = peticionfamilia.executeQuery();
        while(resultat.next()){
            System.out.println(resultat.getInt("codi")+"_"+ resultat.getString("nom"));
            cargarChoise(resultat);
        }
    }

    private void ordre() throws SQLException {
        PreparedStatement peticionordre = null;
        try {
            peticionordre = con.prepareStatement("SELECT * FROM ordres WHERE familia=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionordre.setInt(1,1);
        resultat = peticionordre.executeQuery();
        while(resultat.next()){
            System.out.println(resultat.getInt("familia")+"_"+ resultat.getString("nom"));
            itemsOrdre.add(resultat.getString("nom"));
        }
        comboordre.setItems(itemsOrdre);
        comboordre.getSelectionModel().select(0);
    }

    private void ordreAnimals() throws SQLException {
        PreparedStatement peticionanimals = null;
        try {
            peticionanimals = con.prepareStatement("SELECT * FROM animals WHERE ordre=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionanimals.setInt(1,1);
        resultat = peticionanimals.executeQuery();
        while(resultat.next()){
            Animal animal = new Animal(resultat.getInt("codi"),resultat.getString("nom"),resultat.getInt("ordre"),resultat.getString("especie"),resultat.getString("descripcio"),resultat.getString("estat"),resultat.getString("imatge"));
            animales.add(animal);
        }
    }

    private void estadoAnimal() throws SQLException {
        PreparedStatement peticionestat = null;
        try {
            peticionestat = con.prepareStatement("SELECT DISTINCT estat FROM animals");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultat = peticionestat.executeQuery();
        while(resultat.next()){
            itemsEstat.add(resultat.getString("estat"));
        }
        comboestat.setItems(itemsEstat);
        comboestat.getSelectionModel().select(0);
    }

    private void datosAnimales(){
        textnom.setText(animales.get(numposicio).getNom());
        textdescripcio.setText(animales.get(numposicio).getDescripcio());
        textdescripcio.setWrapText(true);
        textespecie.setText(animales.get(numposicio).getEspecie());
        String url1 = animales.get(numposicio).getImatge();
        String url = correctorUrl(url1);
        Image image= new Image(url);
        System.out.println(url);
        imagen.setImage(image);
    }

    private String correctorUrl(String url){
        String uri1 = url.substring(0,8);
        String uri2 = url.substring(10);
        String urlfinal = uri1+uri2;
        return urlfinal;
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
        textnom.setText(animales.get(numposicio).getNom());
        textdescripcio.setText(animales.get(numposicio).getDescripcio());
        textdescripcio.setWrapText(true);
        textespecie.setText(animales.get(numposicio).getEspecie());
        String url1 = animales.get(numposicio).getImatge();
        String url = correctorUrl(url1);
        Image image= new Image(url);
        System.out.println(url);
        imagen.setImage(image);
    }

    @FXML
    public void buscarSiguiente(Event event){
        textnom.setText(animales.get(numposicio+1).getNom());
        textdescripcio.setText(animales.get(numposicio+1).getDescripcio());
        textdescripcio.setWrapText(true);
        textespecie.setText(animales.get(numposicio+1).getEspecie());
        String url1 = animales.get(numposicio+1).getImatge();
        String url = correctorUrl(url1);
        Image image= new Image(url);
        System.out.println(url);
        imagen.setImage(image);
    }
//rebase
}
