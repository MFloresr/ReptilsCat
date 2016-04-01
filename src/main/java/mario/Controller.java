package mario;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
    private ArrayList<Ordre> ordres = new ArrayList<Ordre>();
    private ArrayList<String> estats = new ArrayList<String>();
    private Animal animal = new Animal();
    private int numposicio=0;
    private String nombreAnimal;
    private int catanimales = 0;
    private int orden = 0;
    @FXML
    public void initialize() {
        try {
            buscarFamilias();
            buscarOrdre();
            animalsDeCadaOrdre();
            estadoAnimal();
            cambiarEstadoBoton();
            datosAnimales();
            btnguardarcambios.setDisable(true);

            choisefamilia.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    try {
                        buscarOrdre();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });

            comboordre.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                    try {
                        System.out.println("ordre: "+ comboordre.getSelectionModel().getSelectedIndex());
                        animalsDeCadaOrdre();
                        datosAnimales();
                        cambiarEstadoBoton();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            comboestat.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    btnguardarcambios.setDisable(false);
                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirConexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cendrassos", "mario", "qcmmer2sa!");
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

    private void buscarFamilias() throws SQLException {
        abrirConexion();
        PreparedStatement peticionfamilia = null;
        try {
            peticionfamilia = con.prepareStatement("SELECT * FROM families");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultat = peticionfamilia.executeQuery();
        while(resultat.next()){
            cargarChoise(resultat);
            //System.out.println(resultat.getString("nom"));
        }
        cerrarConexion();
    }

    private void cargarChoise(ResultSet resultat) throws SQLException {
        choisefamilia.getItems().add(resultat.getString("nom"));
        choisefamilia.getSelectionModel().select(0);
    }

    private void buscarOrdre() throws SQLException {
        abrirConexion();
        PreparedStatement peticionordre = null;
        catanimales= choisefamilia.getSelectionModel().getSelectedIndex()+1;
        System.out.println( "familia que tendria que salir"+catanimales);
        try {
            peticionordre = con.prepareStatement("SELECT * FROM ordres WHERE familia=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionordre.setInt(1,catanimales);
        itemsOrdre.clear();
        ordres.clear();
        resultat = peticionordre.executeQuery();

        while(resultat.next()){
            itemsOrdre.add(resultat.getString("nom"));
            Ordre ordre = new Ordre(resultat.getInt("codi"),resultat.getString("nom"));
            ordres.add(ordre);
            orden = resultat.getInt("codi");
        }
        comboordre.setItems(itemsOrdre);
        comboordre.getSelectionModel().select(0);
        cerrarConexion();
    }

    private void animalsDeCadaOrdre() throws SQLException {
        abrirConexion();
        PreparedStatement peticionanimals = null;
        nombreAnimal = comboordre.getSelectionModel().getSelectedItem().toString();
        System.out.println("este en teoria es el contenido : "+ nombreAnimal);
        for(int i =0; i<ordres.size(); i++){
            if(ordres.get(i).getNom().contains(nombreAnimal)){
                orden = ordres.get(i).getCodi();
            }
        }

        try {
            peticionanimals = con.prepareStatement("SELECT * FROM animals WHERE ordre=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peticionanimals.setInt(1,orden);
        resultat = peticionanimals.executeQuery();
        animales.clear();
        while(resultat.next()){
            Animal animal = new Animal(resultat.getInt("codi"),resultat.getString("nom"),resultat.getInt("ordre"),resultat.getString("especie"),resultat.getString("descripcio"),resultat.getString("estat"),resultat.getString("imatge"));
            animales.add(animal);
        }
        numposicio=0;
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
            estats.add(resultat.getString("estat"));
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
        imagen.setImage(image);

        animal = animales.get(numposicio);

        String estadoAnimal = animales.get(numposicio).getEstat();
        for(int i = 0;i<comboestat.getItems().size();i++){
            if(comboestat.getItems().get(i).equals(estadoAnimal)){
                comboestat.getSelectionModel().select(i);
            }
        }
    }

    private void cambiarEstadoBoton(){
        if(animales.size()==1){
            btnanterior.setDisable(true);
            btnsiguiente.setDisable(true);
        }
        if(animales.size()>1){
            btnanterior.setDisable(true);
            btnsiguiente.setDisable(false);
        }
    }

    private String correctorUrl(String url){
        String uri1 = url.substring(0,8);
        String uri2 = url.substring(10);
        String urlfinal = uri1+uri2;
        return urlfinal;
    }

    @FXML
    public void buscarAnterior(Event event){
        numposicio= numposicio-(1);
        if(numposicio==0){
            btnanterior.setDisable(true);
            datosAnimales();
        }else{
            btnsiguiente.setDisable(false);
            datosAnimales();
        }
    }

    @FXML
    public void buscarSiguiente(Event event){
        numposicio= numposicio+1;
        if(numposicio==animales.size()-1){
            btnsiguiente.setDisable(true);
            datosAnimales();
        }else{
            btnanterior.setDisable(false);
           datosAnimales();
        }
    }

    @FXML
    public void guardarCambios(Event event) throws SQLException {
        abrirConexion();
        PreparedStatement update = null;

        try {
            update = con.prepareStatement("UPDATE animals SET nom=?, especie=?,descripcio=?,estat=? WHERE codi=?");
            String nom = textnom.getText();
            update.setString(1,nom);
            String especie = textespecie.getText();
            update.setString(2,especie);
            String descripcio = textdescripcio.getText();
            update.setString(3,descripcio);
            String estat = comboestat.getSelectionModel().getSelectedItem().toString();
            update.setString(4,estat);
            int codi = 0;
            codi = animal.getCodi();
            update.setInt(5,codi);
            update.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        btnguardarcambios.setDisable(true);
        cerrarConexion();

    }

    public void cambios(Event event ){
        btnguardarcambios.setDisable(false);
    }

}
