package skeletor;


import skeletor.Food.Meal;
import skeletor.Person.*;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Jarek on 2016-12-02.
 */
public class ControlPanel {

    private static int orderNumber = 1;


    private static LinkedList<DinnerKit> menu = new LinkedList<>();
    private static LinkedList<Order> orderLinkedList = new LinkedList<>();
    private static LinkedList<Thread> threads = new LinkedList<>();

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

    public static LinkedList<Order> getOrderLinkedList() {
        return orderLinkedList;
    }

    public static void setOrderLinkedList(LinkedList<Order> orderLinkedList) {
        ControlPanel.orderLinkedList = orderLinkedList;
    }

    public static void main(String[] args) {
        Object guardian = new Object();

        LinkedList<Client> clients_list = new LinkedList<>();
        LinkedList<Deliverer> deliverers_list = new LinkedList<>();
        LinkedList<Meal> meals_list = new LinkedList<>();

        RandomGenerator randomGenerator = new RandomGenerator();

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
        for (int i = 0; i<10; i++){
            randomGenerator.addRandomDeliverer(deliverers_list);
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
        Random random = new Random(System.currentTimeMillis());
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
            getThreads().getLast().start();
            System.out.println(i);
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
        randomGenerator.displayMeal(meals_list);

    }


    public static LinkedList<Thread> getThreads() {
        return threads;
    }

    public static void setThreads(LinkedList<Thread> threads) {
        ControlPanel.threads = threads;
    }
}
