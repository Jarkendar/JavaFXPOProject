package packetGUI;

import javafx.application.Application;
import javafx.application.Platform;
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

import static java.lang.Thread.sleep;


public class Main extends Application {
    // strażnicy
    private final static Object guardian_client = new Object();
    private final static Object guardian_meal = new Object();
    private final static Object guardian_DinnerKit = new Object();
    private final static Object guardian_deliverer = new Object();
    private final static Object guardian_Map = new Object();

    //sekcje krytyczne
    private static volatile int orderNumber = 1;

    private static volatile Map map;
    private static volatile LinkedList<DinnerKit> menu = new LinkedList<>();
    private static volatile LinkedList<Order> orderLinkedList = new LinkedList<>();
    private static volatile LinkedList<Vehicle> vehicles = new LinkedList<>();
    private static volatile LinkedList<Client> clients_list = new LinkedList<>();
    private static volatile LinkedList<Meal> meals_list = new LinkedList<>();
    private static volatile LinkedList<Deliverer> deliverers_list = new LinkedList<>();

    private static volatile LinkedList<Thread> threadsClient = new LinkedList<>();
    private static volatile LinkedList<Thread> threadsDeliverer = new LinkedList<>();

    //zmienne regulujące
    private static int width = 20, lenght = 20, vehicleNumber = 20;
    private static int lRestaurant, wRestaurant;
    private static RandomGenerator randomGenerator = new RandomGenerator();

    //zmienna zliczająca ilość zleceń usunięć z listy klientów
    private static volatile int countOrderToDeleteClient = 0;

    private static Controller controller;
    //zmienna zabijająca wszystkich klientów
    private static volatile boolean clientCanWork = true;
    private static volatile boolean delivererCanWork = true;
    private static volatile boolean timeStop = true;

    public static LinkedList<Deliverer> getDeliverers_list() {
        return deliverers_list;
    }


    public static boolean isDelivererCanWork() {
        return delivererCanWork;
    }

    /**
     * Metoda dodawania posiłku, dodatkowo tworzy zestaw obiadowy w którym może znaleźć się stworzony posiłek.
     */
    public static void addMealToMealList(){
        synchronized (guardian_meal) {
            randomGenerator.addRandomMeal(meals_list);
            createDinnerKit();
        }
    }

    /**
     * Metoda sprawdza czy można dodać jeszcze jedno zlecenie usunięcia
     * @return true - jeśli można, false -jeśli nie można
     */
    synchronized
    public static boolean canOrderDeleteClient(){
        if (countOrderToDeleteClient == clients_list.size()-1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Metoda dodaje 1 do licznika zleceń usunięcia.
     */
    synchronized
    private static void addToCountOrderToDeleteClient(){
        countOrderToDeleteClient++;
    }

    /**
     * Metoda odejmuje 1 od licznika zleceń usunięcia.
     */
    synchronized
    private static void subFromCountOrderToDeleteClient(){
        countOrderToDeleteClient--;
    }

    synchronized
    public static int getCountOrderToDeleteClient() {
        return countOrderToDeleteClient;
    }


    public static boolean isClientCanWork() {
        return clientCanWork;
    }

    /**
     * Metoda sprawdza rozmiar listy klientów.
     * @return rozmiar listy klientów.
     */
    public static int getSizeOfClientList() {
        return clients_list.size();
    }

    /**
     * Metoda sprawdza rozmiar listy dostawców.
     * @return rozmiar listy dostawców.
     */
    public static int getSizeOfDelivererList(){
        return deliverers_list.size();
    }

    /**
     * Metoda ustwia jednemu klientowi, status braku pozwolenia na egzystencje.
     */
    public static void setClientToNotExist(){
        synchronized (guardian_client) {
            addToCountOrderToDeleteClient();
            for (int i = 0; i<clients_list.size(); i++) {
                if (clients_list.get(i).isCanExist()) {
                    clients_list.get(i).setCanExist(false);
                    break;
                }
            }
        }
    }

    /**
     * Metoda odświeżania mapy.
     */
    public static void pleaseMapToRefresh(){
        synchronized (guardian_Map){
            map.refreshMap(clients_list, deliverers_list);
        }
    }



    /**
     * Metoda dodaje dostawców do listy i włącza im wątek.
     */
    public static void addDelivererToList(){
        synchronized (guardian_deliverer){
            randomGenerator.addRandomDeliverer(deliverers_list, guardian_deliverer);//tworzenie dostawcy
            threadsDeliverer.addLast(new Thread(deliverers_list.getLast()));//dodanie go do listy wątków dostawców
            threadsDeliverer.getLast().start();//włączenie go
            System.out.println(deliverers_list.size());
        }
    }

    /**
     * Metoda usuwa klienta z listy, zsynchronizoawana obiektem guardian_client.
     */
    public static void delClientFromList(){
        synchronized (guardian_client) {
            int size = clients_list.size();
            for (int i =0 ; i<size; i++) {
                if (threadsClient.size() != 0 && !threadsClient.get(i).isAlive()) {
                    threadsClient.remove(i);
                    clients_list.remove(i);
                    i--;
                    size--;
                    subFromCountOrderToDeleteClient();
                }
            }
        }
    }

    /**
     * Metoda dodaje nowego klienta do listy klientów, zsynchronizowana obiektem guardian_client.
     */
    public static void addClientToList() {
        synchronized (guardian_client) {
            randomGenerator.addRandomClient(clients_list);//tworzenie klienta
            threadsClient.addLast(new Thread(clients_list.getLast()));//dodanie go do listy wątków klientów
            threadsClient.getLast().start();//włączenie go
            System.out.println(clients_list.size());
        }
    }

    /**
     * Getter listy posiłków.
     * @return lista posiłków
     */
    public static LinkedList<Meal> getMeals_list() {
        return meals_list;
    }

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

    /**
     * Metoda czeka na zakończenie się wszystkich wątków klientów. Jeśli wątek się zakończył to usuwa go z listy wątków
     * klientów.
     */
    synchronized
    public static void clearClientThreadListFromEndProcess() {
        while (threadsClient.size() != 0) {
            for (int i = 0; i < threadsClient.size(); i++) {
                if (!threadsClient.get(i).isAlive()) {
                    threadsClient.remove(i);
                    System.out.println("Usunąłem wątek");
                }
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


    //**********************************************************************************************
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("sample.fxml"));

        controller = loader.getController();//załadowanie kontrolera do zmiennej

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
        GridPane gridPaneMap = (GridPane) children.get(11); //wyciągnięcie mapy z view
//        System.out.println(children.size());
        Label coordinateLabel = (Label) children.get(13);
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
                mapsButtons[i][k].setVisible(false);
                mapsButtons[i][k].setId(i + "," + k);
                mapsButtons[i][k].setStyle("-fx-background-color: #fff;");//ustawienie koloru tła przycisku
                mapsButtons[i][k].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //wyświetlenie współrzędnych przycisku, jego ID
                        coordinateLabel.setText("Kliknięto w pole " + ((Button) event.getSource()).getId().toString());
                        System.out.println("Kliknięto w pole " + ((Button) event.getSource()).getId().toString());
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




        map = new Map(width, lenght, wRestaurant, lRestaurant);
        map.setMapGUI(mapsButtons);

//***********TWORZENIE POJAZDÓW RESTAURACJI I NAPEŁNIANIE ZBIORNIKA PALIWA***********
        randomGenerator.createVehicleForRestaurant(vehicles, vehicleNumber);
        for (Vehicle x: vehicles){
            x.fillTankVehicle();
        }
//***********TWORZENIE WSTĘPNEJ LISTY POSIŁKÓW**********
        for (int i = 0; i < 10; i++) {
            randomGenerator.addRandomMeal(meals_list);
            try {
                sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
//***********TWORZENIE WSTĘPNEJ LISTY ZESTAWÓW OBIADOWYCH************
        for (int i = 0; i < 10; i++) {
            createDinnerKit();
        }


        new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("Włączone odświerzanie");
                while (timeStop){
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            delClientFromList();
                            map.refreshMap(clients_list,deliverers_list);
                        }
                    });
                }
            }
        }.start();

        primaryStage.show();
    }

//    public static GridPane getGridPaneMap (FXMLLoader loader) throws IOException{
//
//        return gridPane;
//    }

    /**
     * Metoda tworzy zestaw obiadowy.
     */
    private static void createDinnerKit() {
        synchronized (guardian_DinnerKit) {
            Random random = new Random(System.nanoTime());
            int count_of_meal = random.nextInt(4) + 1;
            Meal[] meals_in_DinnerKit = new Meal[count_of_meal];
            for (int k = 0; k < count_of_meal; k++) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                int number_of_meal = random.nextInt(meals_list.size());
                meals_in_DinnerKit[k] = meals_list.get(number_of_meal);
            }
            menu.addLast(new DinnerKit((byte) (menu.size() + 1), meals_in_DinnerKit));
        }
    }

    /**
     * Metoda wywoływana automatycznie na koniec działania programu. Na kliknięcie przycisku X.
     *
     * @throws Exception musi być
     */
    @Override
    public void stop() throws Exception {

        timeStop = false;
        clientCanWork = false;
        delivererCanWork = false;
        clearClientThreadListFromEndProcess();
        System.out.println("Koniec");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


