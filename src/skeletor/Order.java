package skeletor;

import java.math.BigDecimal;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Order {
    private int number;
    private String address;
    private long orderTime;
    private DinnerKit[] kit_list;
    private long readyTime;

    /**
     * Metoda ustawia czas gotowości zamówienia, na zasadzie wyciągnięcia czsu najpóźniejszego.
     */
    private void setReadyTime(){
        long maxPreparationTime = 0;
        for (int i = 0; i<kit_list.length; i++){
            for(int j = 0; j<kit_list[i].getMeal_list().length; j++){
                if (maxPreparationTime < kit_list[i].getMeal_list()[j].getPreparation_time()){
                    maxPreparationTime = kit_list[i].getMeal_list()[j].getPreparation_time();
                }
            }
        }
        this.readyTime = maxPreparationTime*10 + orderTime;
    }


    /**
     * Metoda wyświetlająca wszystkie dane zamówienia, wraz z listą zestawów zawierających się w zestawie.
     */
    public void displayOrder(){
        System.out.println("Numer zamówienia: "+number + "; adres dostawy: "+address
                +"; czas zamówienia: "+ orderTime + "; czas gotowości "+ readyTime + "; " );
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
     * @param orderTime - czas dostawy
     * @param kit_list - lista zestawów obiadowych
     */
    public Order(int number, String address,
                 long orderTime, DinnerKit[] kit_list) {
        this.number = number;
        this.address = address;
        this.orderTime = orderTime;
        this.kit_list = kit_list;
        setReadyTime();
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

    public long getReadyTime() {
        return readyTime;
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

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public DinnerKit[] getKit_list() {
        return kit_list;
    }

    public void setKit_list(DinnerKit[] kit_list) {
        this.kit_list = kit_list;
    }
}
