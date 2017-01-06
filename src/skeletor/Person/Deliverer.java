package skeletor.Person;

import skeletor.ControlPanel;
import skeletor.Enums.*;
import skeletor.Transport.Car;
import skeletor.Transport.Scooter;
import skeletor.Transport.Vehicle;

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
    private Object guardian;

    public void setGuardian(Object guardian) {
        this.guardian = guardian;
    }

    @Override
    public void run() {
        do {
            getVehicleFromParking(ControlPanel.getVehicles());
        } while (vehicle == null);
        synchronized (guardian) {
            System.out.print(PESEL + " zabrałem " + vehicle.getRegistration_number() + " ");
            if (vehicle instanceof Car){
                System.out.print("samochód");
            }
            if (vehicle instanceof Scooter){
                System.out.print("skuter");
            }
            System.out.println(" mam uprawnienia na " + getCan_drive());
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(PESEL + " opuszczam "+ vehicle.getRegistration_number());
        leaveVehicleOnParking(ControlPanel.getVehicles());
        if (vehicle == null) {
            System.out.println(PESEL + " teraz mam NULL");
        }
    }

    /**
     * Metoda zostawiania/zwalniania pojazdu na parkingu restauracji.
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

    synchronized
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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
                     E_Dni[] work_day, E_Uprawnienia can_drive, Object guardian){
        super(name, subname);
        this.PESEL = PESEL;
        this.work_day = work_day;
        this.work_hour = work_hour;
        this.can_drive = can_drive;
        this.guardian = guardian;
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
