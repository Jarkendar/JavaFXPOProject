package skeletor;


import skeletor.Food.Meal;
import skeletor.Person.*;
import skeletor.Transport.Vehicle;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Jarek on 2016-12-02.
 */
public class ControlPanel {
    //sekcje krytyczne
    private static volatile int orderNumber = 1;

    private static volatile LinkedList<DinnerKit> menu = new LinkedList<>();
    private static volatile LinkedList<Order> orderLinkedList = new LinkedList<>();
    private static volatile LinkedList<Thread> threads = new LinkedList<>();
    private static volatile LinkedList<Vehicle> vehicles = new LinkedList<>();

    //zmienne regulujące
    private static int width = 50, lenght = 50, vehicleNumber = 30;
    private static int lRestaurant, wRestaurant;

    public static int getlRestaurant() {
        return lRestaurant;
    }

    public static void setRestaurantPosition() {
        Random random = new Random(System.nanoTime());
        ControlPanel.wRestaurant = random.nextInt(getWidth());
        ControlPanel.lRestaurant = random.nextInt(getLenght());
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
        ControlPanel.vehicles = vehicles;
    }

    synchronized
    public static void clearThreadListFromEndProcess(){
        for (int i = 0; i<threads.size(); i++){
            if (!threads.get(i).isAlive()){
                threads.remove(i);
            }
        }
    }

    synchronized
    public static int getOrderNumber() {
        return orderNumber++;
    }

    synchronized
    public static void addOrderToList(Order order){
        orderLinkedList.addLast(order);
    }

    synchronized
    public static LinkedList<DinnerKit> getMenu() {
        return menu;
    }

    public static void setMenu(LinkedList<DinnerKit> menu) {
        ControlPanel.menu = menu;
    }

    synchronized
    public static LinkedList<Order> getOrderLinkedList() {
        return orderLinkedList;
    }

    public static void setOrderLinkedList(LinkedList<Order> orderLinkedList) {
        ControlPanel.orderLinkedList = orderLinkedList;
    }

    public static void main(String[] args) {
        Object guardian = new Object();
        setRestaurantPosition();

        Random random = new Random(System.nanoTime());
        LinkedList<Client> clients_list = new LinkedList<>();
        LinkedList<Deliverer> deliverers_list = new LinkedList<>();
        LinkedList<Meal> meals_list = new LinkedList<>();

        RandomGenerator randomGenerator = new RandomGenerator();
//***********TWORZENIE POJAZDÓW RESTAURACJI***********
        randomGenerator.createVehicleForRestaurant(vehicles, getVehicleNumber());
//**********TWORZENIE LISTY KLIENTÓW**********************
        for (int i = 0 ; i<10; i++){
            randomGenerator.addRandomClient(clients_list);
            //zabezpieczenie przed losowanie zmiennych o tym samym seedzie randoma
            try {
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }
//***********TWORZENIE LISTY DOSTAWCÓW*******************
        for (int i = 0; i<50; i++){
            randomGenerator.addRandomDeliverer(deliverers_list, guardian);
            try{
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }
//***********TWORZENIE WSTĘPNEJ LISTY POSIŁKÓW**********
        for (int i = 0; i<10; i++){
            randomGenerator.addRandomMeal(meals_list);
            try{
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }
//***********TWORZENIE WSTĘPNEJ LISTY ZESTAWÓW OBIADOWYCH************
        for (int i = 0; i<10; i++){
            int count_of_meal = random.nextInt(4)+1;
            Meal[] meals_in_DinnerKit = new Meal[count_of_meal];
            for (int k = 0; k<count_of_meal; k++){
                try{
                    sleep(1);
                }catch (InterruptedException e){
                    System.out.println(e);
                }
                int number_of_meal = random.nextInt(meals_list.size());
                meals_in_DinnerKit[k] = meals_list.get(number_of_meal);
            }
            menu.addLast(new DinnerKit((byte)(i+1),meals_in_DinnerKit));
        }
//***********WYŚWIETLENIE ZESTAWÓW OBIADOWYCH***************
        for (DinnerKit x: menu){
            System.out.println(x.getKit_number());
            System.out.println(x.calculateKitPrice());
            System.out.println(x.calculateKitWeight());
            x.displayNameMeals();
        }
//**********TWORZENIE LISTY WĄTKÓW********************
        System.out.println("Wątek");
        for (int i = 0; i<clients_list.size();i++){
            getThreads().addLast(new Thread(clients_list.get(i)));
        }
        for (int i = 0; i<deliverers_list.size(); i++){
            getThreads().addLast(new Thread(deliverers_list.get(i)));
        }
//**********ODPALENIE WĄTKÓW WSZYSTKICH**************
        for (int i = 0; i<getThreads().size(); i++){
            getThreads().get(i).start();
        }


//***********PĘTLA CZEKANIA NA WSZYSTKIE WĄTKI********
        while (getThreads().size() != 0){
            try {
                getThreads().getFirst().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearThreadListFromEndProcess();
        }



        for (Order x: orderLinkedList){
            x.displayOrder();
        }
//        randomGenerator.displayDeliverers(deliverers_list);
//        randomGenerator.displayClient(clients_list);
//        randomGenerator.displayMeal(meals_list);
        randomGenerator.displayVehicle(vehicles);
        System.out.println(orderLinkedList.size());

        Map map = new Map(width,lenght, wRestaurant, lRestaurant);
        System.out.flush();
        map.addClientToMap(clients_list);
        map.displayMap();

    }


    public static LinkedList<Thread> getThreads() {
        return threads;
    }

    public static void setThreads(LinkedList<Thread> threads) {
        ControlPanel.threads = threads;
    }
}
