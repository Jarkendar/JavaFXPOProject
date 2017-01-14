package packetGUI;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.util.Observable;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("sample.fxml"));

        Controller controller = loader.getController();//załadowanie kontrolera do zmiennej

        GridPane gridPane = loader.load(); //ładowanie GridPanel do zmiennej
//        gridPane.getChildren().addAll( tablica, albo lista kontrolek);// dodanie wsyztskich kontrolek do gridPane
//        GridPane.setConstraints(new Button("ASd"), 20,20);// dodanie kontrolki do konkretnej kolumny i wiersza GridPane
//        gridPane.addColumn(index kolumny, lista kontrolek);// dodanie wszystkiego do jednej konkretnej kolumny
//        ColumnConstraints columnConstraints = new ColumnConstraints(300);//stworzenie kolumny
//        gridPane.getColumnConstraints().add(columnConstraints);//dodanie kolumny do gridPane
//        RowConstraints rowConstraints = new RowConstraints(50);//tworzenie wiersza
//        rowConstraints.setValignment(VPos.BOTTOM);//ustawienie pozycji wiersza
        ObservableList children = gridPane.getChildren();
//        System.out.println("dzieci grida głównego "+children.toString());
        AnchorPane anchorPane_child_scene = (AnchorPane) children.get(0);
        children = anchorPane_child_scene.getChildren();
//        System.out.println("dzieci anchora " + children.toString());
        GridPane gridPaneMap = (GridPane) children.get(12); //wyciągnięcie mapy z view
//        System.out.println("gridpanelMap "+gridPaneMap.toString());
        gridPaneMap.setAlignment(Pos.CENTER);
        //25 rzędów | 30 kolumn
        Button[][] mapsButtons = new Button[20][20];
        for (int i = 0; i< 20; i++){
            for (int k = 0; k<20; k++){
                mapsButtons[i][k] = new Button("0");
                mapsButtons[i][k].setPrefHeight(20);
                mapsButtons[i][k].setPrefWidth(20);
                //mapsButtons[i][k].setVisible(false);
                mapsButtons[i][k].setId(i+","+k);
                mapsButtons[i][k].setStyle("-fx-background-color: #fff;");//ustawienie koloru tła przycisku
                mapsButtons[i][k].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //wyświetlenie współrzędnych przycisku, jego ID
                        System.out.println(((Button)event.getSource()).getId().toString());
                    }
                });
                gridPaneMap.add(mapsButtons[i][k], i, k);//dodanie do grid panelu przycisku
            }
        }
//        Button button = new Button("1");
//        button.setPrefWidth(20);
//        button.setPrefHeight(20);
//        gridPaneMap.add(button, 0, 0);

        //StringProperty stringProperty = new SimpleStringProperty(obiekt, klucz, nazwa);//bardzo ciekawy obiekt stringa z automatyczną aktualizacją


        Scene scene = new Scene(gridPane);//tworzenie sceny z GridPane

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));//ładowanie root z lokacją

        primaryStage.setTitle("Project PO JAVAFX");
        primaryStage.setScene(scene);//ładowanie z GridPane
//        primaryStage.setScene(new Scene(root, 900, 750));//ładowanie z root


//        ObservableList observableList = gridPane.getChildren();
//        AnchorPane anchorPane = (AnchorPane) observableList.get(0);
//        System.out.println(anchorPane.getChildren().toString());//wyciąganie dzieci z obiektu


//        primaryStage.setOpacity(0.9);// ustawienie przezroczystości
//        primaryStage.initStyle(StageStyle.DECORATED); //ustawienie dekoracji stage
        primaryStage.setResizable(false);//blokuje możliwość zmiany rozmiaru stage
//        primaryStage.setX(0);
//        primaryStage.setY(0);//ustawiają domyślne miejsca przymocowania aplikacji

        primaryStage.show();
    }

//    public static GridPane getGridPaneMap (FXMLLoader loader) throws IOException{
//
//        return gridPane;
//    }

    public static void main(String[] args) {
        launch(args);
    }
}


