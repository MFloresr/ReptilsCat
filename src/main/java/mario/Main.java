package mario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Rèptils amfibis de Catalunya");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}