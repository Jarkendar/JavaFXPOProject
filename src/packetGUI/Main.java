package packetGUI;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import skeletor.*;
import skeletor.Food.Meal;
import skeletor.Person.Client;
import skeletor.Person.Deliverer;
import skeletor.Transport.Vehicle;

import java.util.LinkedList;
import java.util.Random;


public class Main extends Application {

    //sekcje krytyczne
    private static volatile int orderNumber = 1;

    private static volatile Map map;
    private static volatile LinkedList<DinnerKit> menu = new LinkedList<>();
    private static volatile LinkedList<Order> orderLinkedList = new LinkedList<>();
    private static volatile LinkedList<Thread> threads = new LinkedList<>();
    private static volatile LinkedList<Vehicle> vehicles = new LinkedList<>();
    private static volatile LinkedList<Client> clients_list = new LinkedList<>();

    //zmienne regulujące
    private static int width = 20, lenght = 20, vehicleNumber = 20;
    private static int lRestaurant, wRestaurant;
    private static RandomGenerator randomGenerator = new RandomGenerator();

    public static Map getMap() {
        return map;
    }

    public static LinkedList<Client> getClients_list() {
        return clients_list;
    }

    public static void setClients_list(LinkedList<Client> clients_list) {
        Main.clients_list = clients_list;
    }

    public static int getlRestaurant() {
        return lRestaurant;
    }

    public static void setRestaurantPosition() {
        Random random = new Random(System.nanoTime());
        Main.wRestaurant = random.nextInt(getWidth());
        Main.lRestaurant = random.nextInt(getLenght());
    }

    public static int getwRestaurant() {
        return wRestaurant;
    }

    public static int getVehicleNumber() {
        return vehicleNumber;
    }

    public static int getWidth() {
        return width;
    }

    public static int getLenght() {
        return lenght;
    }

    synchronized
    public static LinkedList<Vehicle> getVehicles() {
        return vehicles;
    }

    public static void setVehicles(LinkedList<Vehicle> vehicles) {
        Main.vehicles = vehicles;
    }

    synchronized
    public static void clearThreadListFromEndProcess() {
        for (int i = 0; i < threads.size(); i++) {
            if (!threads.get(i).isAlive()) {
                threads.remove(i);
            }
        }
    }

    synchronized
    public static int getOrderNumber() {
        return orderNumber++;
    }

    synchronized
    public static void addOrderToList(Order order) {
        orderLinkedList.addLast(order);
    }

    synchronized
    public static LinkedList<DinnerKit> getMenu() {
        return menu;
    }

    public static void setMenu(LinkedList<DinnerKit> menu) {
        Main.menu = menu;
    }

    synchronized
    public static LinkedList<Order> getOrderLinkedList() {
        return orderLinkedList;
    }

    public static void setOrderLinkedList(LinkedList<Order> orderLinkedList) {
        Main.orderLinkedList = orderLinkedList;
    }

    public static LinkedList<Thread> getThreads() {
        return threads;
    }

    public static void setThreads(LinkedList<Thread> threads) {
        Main.threads = threads;
    }

    //**********************************************************************************************
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        System.out.println("dzieci anchora " + children.toString());
        GridPane gridPaneMap = (GridPane) children.get(12); //wyciągnięcie mapy z view
        System.out.println(children.size());
        Label coordinateLabel = (Label) children.get(14);
//        System.out.println("gridpanelMap "+gridPaneMap.toString());
        gridPaneMap.setAlignment(Pos.CENTER);
        //25 rzędów | 30 kolumn

        Button[][] mapsButtons = new Button[width][lenght];

//USTAWIENIE MAPY WEJSCIOWEJ
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < lenght; k++) {
                mapsButtons[i][k] = new Button("0");
                mapsButtons[i][k].setPrefHeight(20);
                mapsButtons[i][k].setPrefWidth(20);
                //mapsButtons[i][k].setVisible(false);
                mapsButtons[i][k].setId(i + "," + k);
                mapsButtons[i][k].setStyle("-fx-background-color: #fff;");//ustawienie koloru tła przycisku
                mapsButtons[i][k].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //wyświetlenie współrzędnych przycisku, jego ID
                        coordinateLabel.setText("Kliknięto w pole " + ((Button) event.getSource()).getId().toString());
                        System.out.println(((Button) event.getSource()).getId().toString());
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


//***********KOD PROGRAMU ****************************
        Object guardian = new Object();
        setRestaurantPosition();

        Random random = new Random(System.nanoTime());

        LinkedList<Deliverer> deliverers_list = new LinkedList<>();
        LinkedList<Meal> meals_list = new LinkedList<>();

        RandomGenerator randomGenerator = new RandomGenerator();

        Map map = new Map(width, lenght, wRestaurant, lRestaurant);
        map.setMapGUI(mapsButtons);

        primaryStage.show();
    }

//    public static GridPane getGridPaneMap (FXMLLoader loader) throws IOException{
//
//        return gridPane;
//    }


    /**
     * Metoda wywoływana automatycznie na koniec działania programu. Na kliknięcie przycisku X.
     *
     * @throws Exception musi być
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Koniec");
    }

    public static void main(String[] args) {
        launch(args);
    }
}


