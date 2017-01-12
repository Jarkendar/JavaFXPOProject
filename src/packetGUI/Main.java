package packetGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.plugin.javascript.navig.Anchor;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("sample.fxml"));

        Controller controller = loader.getController();//załadowanie kontrolera do zmiennej

        GridPane gridPane = loader.load(); //ładowanie GridPanel do zmiennej

        Scene scene = new Scene(gridPane);//tworzenie sceny z GridPane

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));//ładowanie root z lokacją

        primaryStage.setTitle("Project PO JAVAFX");
        primaryStage.setScene(scene);//ładowanie z GridPane
//        primaryStage.setScene(new Scene(root, 900, 750));//ładowanie z root


//        primaryStage.setOpacity(0.9);// ustawienie przezroczystości
//        primaryStage.initStyle(StageStyle.DECORATED); //ustawienie dekoracji stage
//        primaryStage.setResizable(true);//blokuje możliwość zmiany rozmiaru stage
//        primaryStage.setX(0);
//        primaryStage.setY(0);//ustawiają domyślne miejsca przymocowania aplikacji

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


