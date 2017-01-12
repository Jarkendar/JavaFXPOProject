package skeletor.Person;

import skeletor.ControlPanel;
import skeletor.DinnerKit;
import skeletor.Order;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Client extends Human implements Runnable {

    private int code;
    private int number_order;
    private long time_order;
    private String address;
    private String email = "";
    private Order myOrder;

    @Override
    public void run() {
        int number_of_try = 0;
        Random random = new Random(System.nanoTime());
        while (true){
            //czas na zastanowienie i złożenie zamówienia
            int wait_time = random.nextInt(5000)+1000;
            waitTime(wait_time);

            LinkedList<DinnerKit> tmpmenu = ControlPanel.getMenu();

            //tworzenie zamówienia
            int countChoose = random.nextInt(3)+1;
            DinnerKit[] dinnerKits = new DinnerKit[countChoose];
            for (int i = 0 ; i<countChoose; i++){
                int chooseKit = random.nextInt(tmpmenu.size());
                dinnerKits[i] = tmpmenu.get(chooseKit);
            }
            Order order = new Order(ControlPanel.getOrderNumber(),address,System.currentTimeMillis(),dinnerKits);
            ControlPanel.addOrderToList(order);//dodanie zamówienia do listy zamówień

            myOrder = order;

            //czekanie na dostarczenie zamówienia
            while (myOrder!=null){
                waitTime(1000);
            }

            if (number_of_try == 10) break;
            number_of_try++;
        }
    }

    /**
     * Metoda czekania określoną liczbę milisekund.
     * @param time czas czekania w milisekundach
     */
    private void waitTime(int time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Order getMyOrder(){
        return myOrder;
    }

    /**
     * Metoda wywoływana po dostarczeniu zamówienia przez dostawcę. Dostarczenie zamówienia.
     */
    public void getMyOrderFromDeliverer(){
        System.out.println("Zabieram dostarczone zamówienie");
        myOrder = null;
    }

    /**
     * Konstruktor klasy Client Human bez adresu email
     * @param name
     * @param surname
     * @param code -kod zamawiającego
     * @param address - adres dostawy
     */
    public Client(String name, String surname, int code,
                  String address){
        super(name, surname);
        this.code = code;
        this.address = address;
        this.email = "";
    }

    /**
     * Konstruktor klasy Client Human z adresem email
     * @param name
     * @param surname
     * @param code
     * @param address
     * @param email - adres email
     */
    public Client(String name, String surname, int code,
                  String address, String email){
        super(name, surname);
        this.code = code;
        this.address = address;
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNumber_order() {
        return number_order;
    }

    public void setNumber_order(int number_order) {
        this.number_order = number_order;
    }

    public long getTime_order() {
        return time_order;
    }

    public void setTime_order(long time_order) {
        this.time_order = time_order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract BigInteger getAccount_number();

    public abstract long getREGON();

    public abstract int getLoyal_points();

    public abstract String getMessage_address();
}
