package mario;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.plaf.synth.SynthTextAreaUI;
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
    private int ordresanimales=0;
    private int catanimales = 0;

    @FXML
    public void initialize() {
        try {
            familias();
            ordre();
            ordreAnimals();
            datosAnimales();
            estadoAnimal();
            btnanterior.setDisable(true);
            comboordre.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    System.out.println(observable);
                    System.out.println(oldValue);
                    System.out.println(newValue);
                    System.out.println(comboordre.getSelectionModel().getSelectedIndex()+1);
                    try {
                        ordreAnimals();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            choisefamilia.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    System.out.println(observable);
                    System.out.println(oldValue);
                    System.out.println(newValue);
                    try {
                        ordre();
                        ordreAnimals();
                        datosAnimales();
                        estadoAnimal();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });


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
        abrirConexion();
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
        cerrarConexion();
    }

    private void ordre() throws SQLException {
        abrirConexion();
        PreparedStatement peticionordre = null;

        if(catanimales==0){
            catanimales=1;
        }else{
            catanimales= choisefamilia.getSelectionModel().getSelectedIndex()+1;
            itemsOrdre.clear();
        }

        try {
            peticionordre = con.prepareStatement("SELECT * FROM ordres WHERE familia=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionordre.setInt(1,catanimales);
        resultat = peticionordre.executeQuery();
        while(resultat.next()){
            System.out.println(resultat.getInt("familia")+"_"+ resultat.getString("nom"));
            itemsOrdre.add(resultat.getString("nom"));
        }
        comboordre.setItems(itemsOrdre);
        comboordre.getSelectionModel().select(0);
        cerrarConexion();
    }

    private void ordreAnimals() throws SQLException {
        abrirConexion();
        PreparedStatement peticionanimals = null;

        if(ordresanimales==0){
            ordresanimales=1;
        }else{
            ordresanimales= comboordre.getSelectionModel().getSelectedIndex()+1;
            animales.clear();
            System.out.println(ordresanimales);
        }
        try {
            peticionanimals = con.prepareStatement("SELECT * FROM animals WHERE ordre=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionanimals.setInt(1,ordresanimales);
        resultat = peticionanimals.executeQuery();
        while(resultat.next()){
            Animal animal = new Animal(resultat.getInt("codi"),resultat.getString("nom"),resultat.getInt("ordre"),resultat.getString("especie"),resultat.getString("descripcio"),resultat.getString("estat"),resultat.getString("imatge"));
            animales.add(animal);
        }
        numposicio=0;
        datosAnimales();
        cerrarConexion();
    }

    private void estadoAnimal() throws SQLException {
        abrirConexion();
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
        cerrarConexion();
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

        String estadoAnimal = animales.get(numposicio).getEstat();
        for(int i = 0;i<comboestat.getItems().size();i++){
            if(comboestat.getItems().get(i).equals(estadoAnimal)){
                comboestat.getSelectionModel().select(i);
            }
        }
    }

    private String correctorUrl(String url){
        String uri1 = url.substring(0,8);
        String uri2 = url.substring(10);
        String urlfinal = uri1+uri2;
        return urlfinal;
    }

    private void cargarChoise(ResultSet resultat){
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
        numposicio= numposicio-(1);
        if(numposicio<=0){
            btnanterior.setDisable(true);
        }else{
            btnsiguiente.setDisable(false);
            textnom.setText(animales.get(numposicio).getNom());
            textdescripcio.setText(animales.get(numposicio).getDescripcio());
            textdescripcio.setWrapText(true);
            textespecie.setText(animales.get(numposicio).getEspecie());
            String url1 = animales.get(numposicio).getImatge();
            String url = correctorUrl(url1);
            Image image= new Image(url);
            System.out.println(url);
            imagen.setImage(image);

            String estadoAnimal = animales.get(numposicio).getEstat();
            for(int i = 0;i<comboestat.getItems().size();i++){
                if(comboestat.getItems().get(i).equals(estadoAnimal)){
                    comboestat.getSelectionModel().select(i);
                }
            }
        }
    }

    @FXML
    public void buscarSiguiente(Event event){
        numposicio= numposicio+1;
        if(numposicio==(animales.size()-1)){
            btnsiguiente.setDisable(true);
        }else{
            btnanterior.setDisable(false);
            textnom.setText(animales.get(numposicio).getNom());
            textdescripcio.setText(animales.get(numposicio).getDescripcio());
            textdescripcio.setWrapText(true);
            textespecie.setText(animales.get(numposicio).getEspecie());
            String url1 = animales.get(numposicio).getImatge();
            String url = correctorUrl(url1);
            Image image= new Image(url);
            System.out.println(url);
            imagen.setImage(image);

            String estadoAnimal = animales.get(numposicio).getEstat();
            for(int i = 0;i<comboestat.getItems().size();i++){
                if(comboestat.getItems().get(i).equals(estadoAnimal)){
                    comboestat.getSelectionModel().select(i);
                }
            }
        }
    }
}
