package skeletor;

import java.math.BigDecimal;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Order {
    private int number;
    private String address;
    private long delivery_time;
    private DinnerKit[] kit_list;


    public void displayOrder(){
        System.out.println("Numer zamówienia: "+number + "; adres dostawy: "+address
                +"; czas zamówienia: "+delivery_time+ "; ");
        for (DinnerKit x: kit_list){
            System.out.print(" Numer zestawu: " + x.getKit_number()
                    +"; cena zestawu: "+x.calculateKitPrice()
                    +"; waga zestawu: "+x.calculateKitWeight() );
        }
        System.out.println("\nCena zamówienia: "+calculateOrderPrice()+"; waga zestawu: "+calculateOrderWeight());
    }

    /**
     * Konstruktor klasy Order
     * @param number - numer zamówienia (unikalny)
     * @param address - adres dostawy
     * @param delivery_time - czas dostawy
     * @param kit_list - lista zestawów obiadowych
     */
    public Order(int number, String address,
                 long delivery_time, DinnerKit[] kit_list) {
        this.number = number;
        this.address = address;
        this.delivery_time = delivery_time;
        this.kit_list = kit_list;
    }

    /**
     * Metoda oblicza cenę zamówienie na podstawie
     * cen zestawów
     * @return - zwraca cenę zmaówienia
     */
    public BigDecimal calculateOrderPrice(){
        BigDecimal order_price = new BigDecimal(0);
        for (DinnerKit x: this.kit_list) {
            order_price = order_price.add(x.calculateKitPrice());
        }
        return order_price;
    }

    /**
     * Metoda oblicza wagę zemówienia na podstawie
     * wag zestawów
     * @return - zwraca wagę zamówienia
     */
    public float calculateOrderWeight(){
        float order_weight = 0;
        for (DinnerKit x: this.kit_list) {
            order_weight += x.calculateKitWeight();
        }
        return order_weight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(long delivery_time) {
        this.delivery_time = delivery_time;
    }

    public DinnerKit[] getKit_list() {
        return kit_list;
    }

    public void setKit_list(DinnerKit[] kit_list) {
        this.kit_list = kit_list;
    }
}
