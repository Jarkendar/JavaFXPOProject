package skeletor.Person;

import skeletor.ControlPanel;
import skeletor.Enums.*;
import skeletor.Order;
import skeletor.Transport.Car;
import skeletor.Transport.Scooter;
import skeletor.Transport.Vehicle;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import static java.lang.Thread.sleep;


/**
 * Created by Jarek on 2016-12-02.
 */
public class Deliverer extends Human implements Runnable {
    private final long PESEL;
    private int[] work_hour;
    private E_Dni[] work_day;
    private E_Uprawnienia can_drive;
    private Vehicle vehicle = null;
    private final Object guardian;
    private Order delivererOrder;
    private int positionX;
    private int positionY;

    @Override
    public void run() {
//zabranie zamówienia
        Deliverer_Etique: do {

//pętla czekania, jeśli kierowca nie pracuje o aktualnej godzinie, aktualnego dnia to czeka
            while (!canWork()){
                System.out.println(PESEL + " Mam wolne.");
                waitTime(60000);
            }
//zabieranie zamówienia
            do {
                //dostawca idzie sprawdzić czy są jakieś zamówienia, którymi może się zająć
                waitTime(1000);
                synchronized (guardian) {
                    getDelivererOrder(ControlPanel.getOrderLinkedList());
                }
            } while (delivererOrder == null);
            synchronized (guardian) {
                System.out.println(PESEL + " zabrałem zamówienie na ");
                delivererOrder.displayOrder();
                System.out.println();
            }

//zabranie pojazdu z parkingu Restauracji
            do {
                //dostawca idzie sprawdzić czy na parkingu stoją jakieś pojazdy którymi może prowadzić
                waitTime(1000);
                synchronized (guardian) {
                    getVehicleFromParking(ControlPanel.getVehicles());
                }

            } while (vehicle == null);
//sprawdzenie poprawności zabierania pojazdu
            synchronized (guardian) {
                System.out.print(PESEL + " zabrałem " + vehicle.getRegistration_number() + " ");
                if (vehicle instanceof Car) {
                    System.out.print("samochód");
                }
                if (vehicle instanceof Scooter) {
                    System.out.print("skuter");
                }
                System.out.println(" mam uprawnienia na " + getCan_drive());
            }

            //dekompozycja adresu zamówienia
            String address = delivererOrder.getAddress();
            String[] coordinats = address.split(":");

            System.out.println("Mam jechać na " + address);
            System.out.println("Restauracja jest na " +ControlPanel.getwRestaurant()+ " " + ControlPanel.getlRestaurant());
            System.out.println("Jestem na "+ positionX + " " +positionY);

            int addressX = Integer.parseInt(coordinats[0]);
            int addressY = Integer.parseInt(coordinats[1]);
            int velocity = (int)(vehicle.getSpeed())/10;
            System.out.println("velocity = " +velocity);
            int tmpX = ControlPanel.getwRestaurant(), tmpY = ControlPanel.getlRestaurant();
//dostarczenie zamówienia do klienta i powrót
            while (true){
                synchronized (guardian) {
                    //dojazd do klienta z zamówieniem
                    if (delivererOrder != null) {
                        if (addressX != positionX && addressX >= positionX + (addressX - positionX)) {
                            if (ControlPanel.getMap().setDelivererPositionOnMap(positionX, positionY, positionX + (addressX - positionX), positionY, guardian)) {
                                positionX += (addressX - positionX);
                                System.out.println(PESEL + " ruszyłem się w pionie");
                            }
                        }
                        else if (addressY != positionY && addressY >= positionY + (addressY - positionY)) {
                            if (ControlPanel.getMap().setDelivererPositionOnMap(positionX, positionY, positionX, positionY + (addressY - positionY), guardian)) {
                                positionY += (addressY - positionY);
                                System.out.println(PESEL + " ruszyłem się w poziomie");
                            }
                        }//oddanie zamówienia klientowi
                        if ((addressX == positionX) && (addressY == positionY)) {
                            ControlPanel.getMap().addClientToMap(ControlPanel.getClients_list());
                            giveOrderToClient();
                            System.out.println(PESEL + " pora wrócić.");
                        }
                    }//powrót do restauracji z pustym bagażem
                    else if (delivererOrder == null) {
                        if (tmpX != positionX && tmpX >= positionX + (tmpX - positionX)) {
                            if (ControlPanel.getMap().setDelivererPositionOnMap(positionX, positionY, positionX + (tmpX - positionX), positionY, guardian)) {
                                positionX += (tmpX - positionX);
                                System.out.println(PESEL + " wracam w pionie.");
                            }
                        } else if (tmpY != positionY && tmpY >= positionY + (tmpY - positionY)) {
                            if (ControlPanel.getMap().setDelivererPositionOnMap(positionX, positionY, positionX, positionY + (tmpY - positionY), guardian)) {
                                positionY += (tmpY - positionY);
                                System.out.println(PESEL + " wracam w poziomie.");
                            }
                        }
                        if (tmpX == positionX && tmpY == positionY) {
                            System.out.println("Wróciłem do restauracji");
                            break;
                        }
                    }
                }
                System.out.println(PESEL + " " + positionX + "; " + positionY);
                System.out.println("czekam");
                waitTime(5000);//symulacja tur
                System.out.println("skończyłem czekać");
            }
//opuszczenie pojazdu przez dostawcę
            waitTime(1000);
            System.out.println(PESEL + " opuszczam " + vehicle.getRegistration_number());
            synchronized (guardian) {
                leaveVehicleOnParking(ControlPanel.getVehicles());
            }
        }while (true);
    }

    /**
     * Metoda sprawdza czy kierowca może wziąść zamówienie, a potem je rozwieść. Sprawdza czy kierowca pracuje.
     * @return wartość true - jeśli kierowca pracuje w dany dzień o danej godzinie, false - w przeciwnym wypadku
     */
    private boolean canWork(){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean canHour = false, canDay = false;
        Etiq_day:for (int i = 0; i<work_day.length; i++){
            switch (work_day[i]){
                case niedziela:{
                    if (day == 1) {
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case poniedziałek:{
                    if (day == 2){
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case wtorek:{
                    if (day == 3){
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case środa:{
                    if (day == 4){
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case czwartek:{
                    if (day == 5){
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case piątek:{
                    if (day == 6){
                        canDay = true;
                        break Etiq_day;
                    }
                }
                case sobota:{
                    if (day == 7){
                        canDay = true;
                        break Etiq_day;
                    }
                }
            }
        }
        for (int i = 0; i<work_hour.length; i++){
            if (hour == work_hour[i]){
                canHour = true;
                break;
            }
        }
        return (canDay && canHour);
    }

    /**
     * Metoda szuka klienta, daje mu zamówienie i usuwa zamówienie z bagażu dostawcy.
     */
    private void giveOrderToClient(){
        synchronized (guardian) {
            for (Client x : ControlPanel.getClients_list()) {
                if (x.getMyOrder() == delivererOrder) {
                    x.getMyOrderFromDeliverer();
                    delivererOrder = null;
                    System.out.println(PESEL + " dałem klientowi zamówienie.");
                    break;
                }
            }
        }
    }

    /**
     * Metoda zabiera jedno zamówienie z listy zamówień, sprawdza czy dostawca może je zabrać.
     * @param orders lista zamówień
     */
    private void getDelivererOrder (LinkedList<Order> orders){
        synchronized (guardian) {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getReadyTime() < System.currentTimeMillis()) {
                    //zabezpieczenie przed wzięciem zamówienia za ciężkiego dla pojazdu na który ma uprawnienia
                    if (this.getCan_drive().equals(E_Uprawnienia.skuter) && orders.get(i).calculateOrderWeight()<= 50.0){
                        this.delivererOrder = orders.get(i);
                        orders.remove(i);
                        System.out.println("Zabrałem zamówienie");
                        break;
                    }else if (this.can_drive.equals(E_Uprawnienia.samochód) && orders.get(i).calculateOrderWeight() <= 100.0){
                        this.delivererOrder = orders.get(i);
                        orders.remove(i);
                        System.out.println("Zabrałem zamówienie");
                        break;
                    }
                }
            }
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

    /**
     * Metoda zostawiania/zwalniania pojazdu na parkingu restauracji i go tankuje.
     * @param vehicles lista pojazdów na parkingu
     */
    public void leaveVehicleOnParking(LinkedList<Vehicle> vehicles){
        synchronized (guardian) {
            vehicles.addLast(this.vehicle);
            vehicles.getLast().fillTankVehicle();
            this.vehicle = null;
        }
    }

    /**
     * Metoda pobierania pojazdu z parkingu restauracji.
     * @param vehicles lista pojazdów na parkingu
     * @return wartość boolean określająca czy udało się pobrać samochód
     */
    public boolean getVehicleFromParking (LinkedList<Vehicle> vehicles){
        synchronized (guardian) {
            for (int i = 0; i < vehicles.size(); i++) {
                if (vehicles.get(i) instanceof Car && this.getCan_drive().equals(E_Uprawnienia.samochód)) {
                    setVehicle(vehicles.get(i));
                    vehicles.remove(i);
                    return true;
                } else if (vehicles.get(i) instanceof Scooter && this.getCan_drive().equals(E_Uprawnienia.skuter)) {
                    setVehicle(vehicles.get(i));
                    vehicles.remove(i);
                    return true;
                }
            }
            return false;
        }
    }

    public Vehicle getVehicle() {
        return vehicle;
    }


    public void setVehicle(Vehicle vehicle) {
        synchronized (guardian) {
            this.vehicle = vehicle;
        }
    }

    /**
     * Konstructor klasy Deliver Person.Human
     * @param name
     * @param subname
     * @param PESEL - indywidualny numer PESEL
     * @param work_hour - godziny pracy
     * @param work_day - dni pracy
     * @param can_drive - uprawnienia prowadzenia pojazdu
     */
    public Deliverer(String name, String subname,
                     long PESEL, int[] work_hour,
                     E_Dni[] work_day, E_Uprawnienia can_drive, Object guardian, int positionX, int positionY){
        super(name, subname);
        this.PESEL = PESEL;
        this.work_day = work_day;
        this.work_hour = work_hour;
        this.can_drive = can_drive;
        this.guardian = guardian;
        this.positionX = positionX;
        this.positionY = positionY;
    }



    public long getPESEL() {
        return PESEL;
    }

    public int[] getWork_hour() {
        return work_hour;
    }

    public void setWork_hour(int[] work_hour) {
        this.work_hour = work_hour;
    }

    public E_Dni[] getWork_day() {
        return work_day;
    }

    public void setWork_day(E_Dni[] work_day) {
        this.work_day = work_day;
    }

    public E_Uprawnienia getCan_drive() {
        return can_drive;
    }

    public void setCan_drive(E_Uprawnienia can_drive) {
        this.can_drive = can_drive;
    }
}
