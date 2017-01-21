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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import skeletor.*;
import skeletor.Enums.E_Dodatki;
import skeletor.Enums.E_KategoriaPosiłku;
import skeletor.Enums.E_RodzajCiasta;
import skeletor.Food.*;
import skeletor.Person.*;
import skeletor.Transport.Car;
import skeletor.Transport.Vehicle;

import java.io.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;


public class Main extends Application {
    VBox containerOnInfo;
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
    private static volatile int countOrderToDeleteDeliverer = 0;

    private static Controller controller;
    //zmienna zabijająca wszystkich klientów
    private static volatile boolean clientCanWork = true;
    private static volatile boolean delivererCanWork = true;
    private static volatile boolean timeStop = true;

    //ścieżki dostępu
    private static final String pathNameClient = "ClientList.txt";
    private static final String pathNameDeliverer = "DelivererList.txt";
    private static final String pathNameOrder = "OrderList.txt";
    private static final String pathNameMeal = "MealList.txt";
    private static final String pathNameDinnerKit = "DinnerKitList.txt";
    private static final String pathNameRestaurant = "RestaurantList.txt";
    private static final String pathNameVehicle = "VehicleList.txt";

    public static LinkedList<Deliverer> getDeliverers_list() {
        return deliverers_list;
    }


    public static boolean isDelivererCanWork() {
        return delivererCanWork;
    }

    /**
     * Metoda dodawania posiłku, dodatkowo tworzy zestaw obiadowy w którym może znaleźć się stworzony posiłek.
     */
    public static void addMealToMealList() {
        synchronized (guardian_meal) {
            randomGenerator.addRandomMeal(meals_list);
            createDinnerKit();
        }
    }

    public static void addMealToMealList(String name, String price, String weight, String category
            , String subcategory, String preparationTime, String[] components) {
        synchronized (guardian_meal) {
            BigDecimal priceB = new BigDecimal(price);
            float weightF = Float.parseFloat(weight);
            long preparationTimeI = Integer.parseInt(preparationTime);
            Meal newMeal = null;
            switch (category) {
                case "Pizza": {
                    switch (subcategory) {
                        case "Margheritta": {
                            newMeal = new Margheritta(name, components, priceB, weightF, E_KategoriaPosiłku.pizza, preparationTimeI, false);
                            break;
                        }
                        case "Salami": {
                            newMeal = new Salami(name, components, priceB, weightF, E_KategoriaPosiłku.pizza, preparationTimeI, false);
                            break;
                        }
                        case "Hawaiian": {
                            newMeal = new Hawaiian(name, components, priceB, weightF, E_KategoriaPosiłku.pizza, preparationTimeI, false);
                            break;
                        }
                    }
                    break;
                }
                case "Makaron": {
                    switch (subcategory) {
                        case "Spaghetti": {
                            newMeal = new Spaghetti(name, components, priceB, weightF, E_KategoriaPosiłku.makaron, preparationTimeI);
                            break;
                        }
                        case "Lasagne": {
                            newMeal = new Lasagne(name, components, priceB, weightF, E_KategoriaPosiłku.makaron, preparationTimeI);
                            break;
                        }
                    }
                    break;
                }
                case "Deser": {
                    switch (subcategory) {
                        case "Ciasto": {
                            newMeal = new Cake(name, components, priceB, weightF, E_KategoriaPosiłku.deser, preparationTimeI, E_RodzajCiasta.Waniliowe);
                            break;
                        }
                        case "Naleśniki": {
                            newMeal = new Pancake(name, components, priceB, weightF, E_KategoriaPosiłku.deser, preparationTimeI, E_Dodatki.ser);
                            break;
                        }
                    }
                }
            }
            meals_list.addLast(newMeal);
        }
    }

    /**
     * Metoda sprawdza czy można dodać jeszcze jedno zlecenie usunięcia dostawcy.
     *
     * @return true - jeśli można, false - jeśli nie można
     */
    synchronized
    public static boolean canOrderDeleteDeliverer() {
        if (countOrderToDeleteDeliverer == deliverers_list.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metoda sprawdza czy można dodać jeszcze jedno zlecenie usunięcia klienta.
     *
     * @return true - jeśli można, false - jeśli nie można
     */
    synchronized
    public static boolean canOrderDeleteClient() {
        if (countOrderToDeleteClient == clients_list.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metoda dodaje 1 do licznika zleceń usunięcia dostawców.
     */
    synchronized
    private static void addToCountOrderToDeleteDeliverer() {
        countOrderToDeleteDeliverer++;
    }

    /**
     * Metoda odejmuje 1 od licznika zleceń usunięcia dostawców.
     */
    synchronized
    private static void subFromCountOrderToDeleteDeliverer() {
        countOrderToDeleteDeliverer--;
    }

    /**
     * Metoda dodaje 1 do licznika zleceń usunięcia klientów.
     */
    synchronized
    private static void addToCountOrderToDeleteClient() {
        countOrderToDeleteClient++;
    }

    /**
     * Metoda odejmuje 1 od licznika zleceń usunięcia klientów.
     */
    synchronized
    private static void subFromCountOrderToDeleteClient() {
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
     *
     * @return rozmiar listy klientów.
     */
    public static int getSizeOfClientList() {
        return clients_list.size();
    }

    /**
     * Metoda sprawdza rozmiar listy dostawców.
     *
     * @return rozmiar listy dostawców.
     */
    public static int getSizeOfDelivererList() {
        return deliverers_list.size();
    }

    /**
     * Metoda ustawia jednemu klientowi status braku pozwolenia na egzystencję.
     */
    public static void setClientToNotExist() {
        synchronized (guardian_client) {
            addToCountOrderToDeleteClient();
            for (int i = 0; i < clients_list.size(); i++) {
                if (clients_list.get(i).isCanExist()) {
                    clients_list.get(i).setCanExist(false);
                    break;
                }
            }
        }
    }

    /**
     * Metoda ustawia jednemu dostawcy status braku pozwolenia na egzystencję.
     */
    public static void setDelivererToNotExist() {
        synchronized (guardian_deliverer) {
            addToCountOrderToDeleteDeliverer();
            for (int i = 0; i < deliverers_list.size(); i++) {
                if (deliverers_list.get(i).isCanExist()) {
                    deliverers_list.get(i).setCanExist(false);
                    break;
                }
            }
        }
    }

    /**
     * Metoda usuwa dostawców z listy dostawców.
     */
    public static void delDelivererFromList() {
        synchronized (guardian_deliverer) {
            int size = deliverers_list.size();
            for (int i = 0; i < size; i++) {
                if (threadsDeliverer.size() != 0 && !threadsDeliverer.get(i).isAlive()) {
                    threadsDeliverer.remove(i);
                    deliverers_list.remove(i);
                    i--;
                    size--;
                    subFromCountOrderToDeleteDeliverer();
                }
            }
        }
    }

    /**
     * Metoda dodaje dostawców do listy i włącza im wątek.
     */
    public static void addDelivererToList() {
        synchronized (guardian_deliverer) {
            randomGenerator.addRandomDeliverer(deliverers_list, guardian_deliverer);//tworzenie dostawcy
            threadsDeliverer.addLast(new Thread(deliverers_list.getLast()));//dodanie go do listy wątków dostawców
            threadsDeliverer.getLast().setDaemon(true);
            threadsDeliverer.getLast().start();//włączenie go
            System.out.println(deliverers_list.size());
        }
    }

    /**
     * Metoda dodaje dostawców do listy i włącza im wątek.
     */
    public static void addDelivererToList(Deliverer x) {
        synchronized (guardian_deliverer) {
            x.setGuardian(guardian_deliverer);
            deliverers_list.addLast(x);//tworzenie dostawcy
            deliverers_list.getLast().setPositionX(wRestaurant);
            deliverers_list.getLast().setPositionY(lRestaurant);
            threadsDeliverer.addLast(new Thread(deliverers_list.getLast()));//dodanie go do listy wątków dostawców
            threadsDeliverer.getLast().setDaemon(true);
            threadsDeliverer.getLast().start();//włączenie go
            System.out.println(deliverers_list.size());
        }
    }

    /**
     * Metoda usuwa klienta z listy, zsynchronizoawana obiektem guardian_client.
     */
    public static void delClientFromList() {
        synchronized (guardian_client) {
            int size = clients_list.size();
            for (int i = 0; i < size; i++) {
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
            threadsClient.getLast().setDaemon(true);
            threadsClient.getLast().start();//włączenie go
            System.out.println(clients_list.size());
        }
    }

    public static void addClientToList(Client x) {
        clients_list.addLast(x);
        threadsClient.addLast(new Thread(clients_list.getLast()));//dodanie go do listy wątków klientów
        threadsClient.getLast().setDaemon(true);
        threadsClient.getLast().start();//włączenie go
        System.out.println(clients_list.size());
    }

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

    private void killVboxInfoChildren() {
        containerOnInfo.getChildren().remove(0, containerOnInfo.getChildren().size());
    }

    /**
     * Myświetlenie danych restauracji.
     */
    private void addRestaurantInfoToVbox() {
        containerOnInfo.getChildren().addAll(new Label("Moja restauracja."), new Label("Położona " + wRestaurant + ":" + lRestaurant));
    }

    /**
     * Wyświetlenie danych klienta.
     *
     * @param client kliknięty klient
     */
    private void addClientInfoToVbox(Client client) {
        Label type;
        if (client instanceof Occasional) {
            type = new Label("OKAZJONALNY");
        } else if (client instanceof Regular) {
            type = new Label("REGULARNY");
        } else {
            type = new Label("FIRMOWY");
        }
        Label order;
        if (client.getMyOrder() == null) {
            order = new Label("Nie zamówił");
        } else {
            order = new Label("Zamówił.");
        }
        containerOnInfo.getChildren().addAll(type, new Label("Imię " + client.getName())
                , new Label("Nazwisko " + client.getSurname()), new Label("Adres " + client.getAddress())
                , new Label("Kod zamawiającego " + client.getCode()), order);
    }

    /**
     * Wyświetlenie danych dostawcy.
     *
     * @param deliverer kliknięty dostawca
     */
    private void addDelivererInfoToVbox(Deliverer deliverer) {
        Label canWork;
        if (deliverer.canWork()) {
            canWork = new Label("Pracuję");
        } else {
            canWork = new Label("Mam wolne");
        }
        Label hasOrder;
        Label orderAddress;
        if (deliverer.getDelivererOrder() == null) {
            hasOrder = new Label("Wracam do restauracji");
            orderAddress = new Label("");
        } else {
            hasOrder = new Label("Jadę do klienta");
            orderAddress = new Label("Jadę do " + deliverer.getDelivererOrder().getAddress());
        }
        Label vehicleType;
        if (deliverer.getVehicle() instanceof Car) {
            vehicleType = new Label("Jadę samochodem " + deliverer.getVehicle().getRegistration_number());
        } else {
            vehicleType = new Label("Jadę skuterem " + deliverer.getVehicle().getRegistration_number());
        }
        containerOnInfo.getChildren().addAll(new Label("DOSTAWCA"), new Label("PESEL " + deliverer.getPESEL())
                , new Label("Imię " + deliverer.getName()), new Label("Nazwisko " + deliverer.getSurname())
                , canWork, hasOrder, vehicleType
                , new Label("Pojemność baku " + String.format("%.2f", deliverer.getVehicle().getActualTankValue()) + " l")
                , orderAddress);
    }

    /**
     * Sprawdzenie co zostało kliknięte na mapie, przez użytkownika.
     *
     * @param buttonCoordinate współrzędne przycisku
     */
    public void checkWhatUserPressOnMap(String buttonCoordinate) {
        String[] tmp = buttonCoordinate.split(",");
        int posX = Integer.parseInt(tmp[0]);
        int posY = Integer.parseInt(tmp[1]);
        Mark:
        while (true) {
            if (posX == wRestaurant && posY == lRestaurant) {
                killVboxInfoChildren();
                addRestaurantInfoToVbox();
                System.out.println("To restauracja.");
                break Mark;
            }
            synchronized (guardian_client) {
                for (Client x : clients_list) {
                    if (x.getAddress().equals(posX + ":" + posY)) {
                        killVboxInfoChildren();
                        addClientInfoToVbox(x);
                        System.out.println("To klient.");
                        break Mark;
                    }
                }
            }
            synchronized (guardian_deliverer) {
                for (Deliverer x : deliverers_list) {
                    if (x.getPositionX() == posX && x.getPositionY() == posY) {
                        killVboxInfoChildren();
                        addDelivererInfoToVbox(x);
                        System.out.println("To dostawca.");
                        break Mark;
                    }
                }
            }
        }
    }

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
     * Wczytuje klientów z pliku i dodaje ich do listy kientów, wraz z włączeniem im wątku.
     */
    private static void readClients() {
        File file = new File(pathNameClient);
        if (file.exists()) {
            try {
                String mark;
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                while (true) {
                    mark = (String) objectInputStream.readObject();
                    System.out.println(mark);
                    if (mark.equals("Occasional")) {
                        Occasional x = (Occasional) objectInputStream.readObject();
                        addClientToList(x);
                    } else if (mark.equals("Regular")) {
                        Regular x = (Regular) objectInputStream.readObject();
                        addClientToList(x);
                    } else if (mark.equals("Corporate")) {
                        Corporate x = (Corporate) objectInputStream.readObject();
                        addClientToList(x);
                    }
                }
            } catch (IOException e) {
                System.out.println("Koniec pliku klientów.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                file = null;
            }
        }
        file = null;
    }

    /**
     * Odczyt dostawców z pliku.
     */
    private static void readDeliverer() {
        File file = new File(pathNameDeliverer);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                while (true) {
                    synchronized (guardian_deliverer) {
                        addDelivererToList(((Deliverer) objectInputStream.readObject()));
                        System.out.println("wczytałem dostawce");
                    }
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        System.out.println("Błąd czekania");
                    }
                }
            } catch (IOException e) {
                System.out.println("Koniec pliku dostawców.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                file = null;
            }
        }
        file = null;
    }

    /**
     * Zapis dostawców do pliku.
     */
    private static void saveDeliverer() {
        File file = new File(pathNameDeliverer);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (Deliverer x : deliverers_list) {
                objectOutputStream.writeObject(x);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = null;
        }
    }

    /**
     * Zapis listy klientów do pliku.
     */
    private static void saveClients() {
        File file = new File(pathNameClient);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (Client x : clients_list) {
                String mark = "";
                if (x instanceof Occasional) {
                    mark = "Occasional";
                } else if (x instanceof Regular) {
                    mark = "Regular";
                } else if (x instanceof Corporate) {
                    mark = "Corporate";
                }
                objectOutputStream.writeObject(mark);
                if (mark.equals("Occasional")) {
                    objectOutputStream.writeObject((Occasional) x);
                } else if (mark.equals("Regular")) {
                    objectOutputStream.writeObject((Regular) x);
                } else if (mark.equals("Corporate")) {
                    objectOutputStream.writeObject((Corporate) x);
                }
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = null;
        }
    }

    /**
     * Odczyt koordynatów restauracji z pliku.
     */
    public static void readRestaurant() {
        File file = new File(pathNameRestaurant);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                wRestaurant = (Integer) objectInputStream.readObject();
                lRestaurant = (Integer) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                file = null;
            }
        }
        file = null;
    }

    /**
     * Zapis koordynatów restauracji do pliku.
     */
    public static void saveRestaurant() {
        File file = new File(pathNameRestaurant);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(wRestaurant);
            outputStream.writeObject(lRestaurant);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = null;
        }
    }

    /**
     * Dodanie pojazdu do listy pojazdów.
     *
     * @param vehicle pojazd wczytany z pliku
     */
    private static void addVehicleToList(Vehicle vehicle) {
        synchronized (guardian_deliverer) {
            vehicles.addLast(vehicle);
        }
    }

    /**
     * Odczyt pojazdów z plików.
     */
    public static void readVehicle() {
        File file = new File(pathNameVehicle);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                while (true) {
                    Vehicle vehicle = (Vehicle) objectInputStream.readObject();
                    addVehicleToList(vehicle);
                }
            } catch (IOException e) {

                System.out.println("Koniec pliku pojazdów.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                file = null;
            }
        }
        file = null;
    }

    /**
     * Zapis pojazdów do plików.
     */
    public static void saveVehicle() {
        File file = new File(pathNameVehicle);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (Vehicle x : vehicles) {
                objectOutputStream.writeObject(x);
            }
            synchronized (guardian_deliverer) {
                for (Deliverer x : deliverers_list) {
                    if (x.getVehicle() != null) {
                        objectOutputStream.writeObject(x.getVehicle());
                    }
                }
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = null;
        }
    }

    private static void addOrderToOrderList(Order order) {
        synchronized (guardian_client) {
            orderLinkedList.addLast(order);
        }
    }

    public static void readOrder() {
        File file = new File(pathNameOrder);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                while (true) {
                    Order order = (Order) objectInputStream.readObject();
                    addOrderToOrderList(order);
                    synchronized (guardian_client) {
                        for (Client x : clients_list) {
                            if (x.getAddress().equals(order.getAddress())) {
                                x.setMyOrder(order);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Koniec pliku zamówień.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {

                file = null;
            }
        }
        file = null;
    }

    /**
     * Zapis zamówień do plików.
     */
    public static void saveOrder() {
        File file = new File(pathNameOrder);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (Order x : orderLinkedList) {
                objectOutputStream.writeObject(x);
            }
            objectOutputStream.close();
            synchronized (guardian_deliverer) {
                for (Deliverer x : deliverers_list) {
                    if (x.getDelivererOrder() != null) {
                        objectOutputStream.writeObject(x.getDelivererOrder());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = null;
        }

    }

    /**
     * Metoda zapisuje wszystkie dane do plików. Wywołuje odpowiednie metody serializacji.
     */
    public static void saveAll() {
        synchronized (guardian_client) {
            saveClients();
        }
        synchronized (guardian_deliverer) {
            saveDeliverer();
        }
        saveRestaurant();
        synchronized (guardian_deliverer) {
            saveVehicle();
        }
        synchronized (guardian_client) {
            saveOrder();
        }
    }

    private static void delAllList() {
        synchronized (guardian_client) {
            for (Client x : clients_list) {
                clients_list.remove(x);
            }
        }
        synchronized (guardian_deliverer) {
            for (Deliverer x : deliverers_list) {
                deliverers_list.remove(x);
            }
        }
        synchronized (guardian_client) {
            for (Order x : orderLinkedList) {
                orderLinkedList.remove(x);
            }
        }
        synchronized (guardian_deliverer) {
            for (Vehicle x : vehicles) {
                vehicles.remove(x);
            }
        }
    }

    public static void readAll() {
        delAllList();
        readRestaurant();
        readVehicle();
        readClients();
        readDeliverer();
        readOrder();
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

    private static void delFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public static void delAllFile() {
        File file = new File(pathNameOrder);
        file.delete();
        file = null;
        File file2 = new File(pathNameRestaurant);
        file2.delete();
        file2 = null;
        File file3 = new File(pathNameClient);
        file3.delete();
        file3 = null;
        File file4 = new File(pathNameDeliverer);
        file4.delete();
        file4 = null;
        File file5 = new File(pathNameVehicle);
        file5.delete();
        file5 = null;
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
        containerOnInfo = (VBox) children.get(14);
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
                        checkWhatUserPressOnMap(((Button) event.getSource()).getId().toString());
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
        for (Vehicle x : vehicles) {
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
                System.out.println("Włączone odświeżanie");
                while (timeStop) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            delClientFromList();
                            delDelivererFromList();
                            map.refreshMap(clients_list, deliverers_list);
                        }
                    });
                }
            }
        }.start();
        primaryStage.show();
        read();



    }

    private static void read(){
        readRestaurant();
        synchronized (guardian_deliverer) {
            readVehicle();
        }
        synchronized (guardian_client) {
            readClients();
        }
        synchronized (guardian_client) {
            readOrder();
        }
        readDeliverer();
    }

//    public static GridPane getGridPaneMap (FXMLLoader loader) throws IOException{
//
//        return gridPane;
//    }

    public static void main(String[] args) {
        launch(args);
    }
}


