package skeletor.Transport;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Scooter extends Vehicle {

    /**
     * Konstruktor klasy Transport.Scooter Transport.Vehicle. _
     * opis parametrów patrz konstruktor Transport.Vehicle
     * @param cargo
     * @param tank_max_value
     * @param speed
     * @param registration_number
     */
    public Scooter(float cargo, float tank_max_value,
                   byte speed, String registration_number){
        super(cargo, tank_max_value,
                speed, registration_number);
    }

    /**
     * Metoda spalania, spala 0.03 na cykl.
     */
    @Override
    public void burnGasoline() {
        setActualTankValue(getTank_max_value()-(float) 0.03);
    }
}
