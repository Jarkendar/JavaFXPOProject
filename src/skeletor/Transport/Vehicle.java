package skeletor.Transport;

import java.io.Serializable;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Vehicle implements Serializable {
    private float cargo;
    private float tank_max_value;
    private byte speed;
    private String registration_number;
    private float actualTankValue = 0;

    public Vehicle() {
    }

    /**
     * Konstruktor klasy Transport.Vehicle
     * @param cargo - ładowność
     * @param tank_max_value - maksymalna pojemność zbiornika paliwa
     * @param speed - prędkość pojazdu
     * @param registration_number -numer rejestracyjny
     */
    public Vehicle(float cargo, float tank_max_value,
                   byte speed, String registration_number){
        this.cargo = cargo;
        this.tank_max_value = tank_max_value;
        this.speed = speed;
        this.registration_number = registration_number;
    }

    public float getActualTankValue() {
        return actualTankValue;
    }

    public void setActualTankValue(float actualTankValue) {
        this.actualTankValue = actualTankValue;
    }

    /**
     * Metoda spalania do nadpisania w klasach potomnych.
     */
    public abstract void burnGasoline();

    /**
     * Metoda napełniania baku do pełna.
     */
    public void fillTankVehicle(){
        actualTankValue = tank_max_value;
    }

    public float getCargo() {
        return cargo;
    }

    public void setCargo(float cargo) {
        this.cargo = cargo;
    }

    public float getTank_max_value() {
        return tank_max_value;
    }

    public void setTank_max_value(float tank_max_value) {
        this.tank_max_value = tank_max_value;
    }

    public byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }
}
