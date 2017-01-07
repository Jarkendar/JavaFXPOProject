package skeletor.Person;

import skeletor.ControlPanel;
import skeletor.DinnerKit;
import skeletor.Order;
import skeletor.RandomGenerator;

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

    @Override
    public void run() {
        int number_of_try = 0;
        Random random = new Random(System.nanoTime());
        while (true){
            int wait_time = random.nextInt(4)+1000;

            try {
                sleep(wait_time);
                LinkedList<DinnerKit> tmpmenu = ControlPanel.getMenu();

                int countChoose = random.nextInt(3)+1;
                DinnerKit[] dinnerKits = new DinnerKit[countChoose];
                for (int i = 0 ; i<countChoose; i++){
                    int chooseKit = random.nextInt(tmpmenu.size());
                    dinnerKits[i] = tmpmenu.get(chooseKit);
                }
                Order order = new Order(ControlPanel.getOrderNumber(),address,System.currentTimeMillis(),dinnerKits);
                ControlPanel.addOrderToList(order);
            }catch (InterruptedException e){
                System.out.println(e);
            }

            if (number_of_try == 10) break;
            number_of_try++;
        }
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
