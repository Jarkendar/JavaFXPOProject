package skeletor.Person;

import java.math.BigInteger;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Regular extends Client {

    private int loyal_points;

    /**
     * Konstruktor klasy Regular Client bez adresu email
     * @param name
     * @param surname
     * @param code
     * @param time_order
     * @param address
     * @param loyal_points - punkty lojalnościowe
     */
    public Regular(String name, String surname, int code,
                      String address,
                      int loyal_points){
        super(name, surname, code,
                address);
        this.loyal_points = loyal_points;
    }

    /**
     * Konstruktor klasy Regular Client z adresem email
     * @param name
     * @param surname
     * @param code
     * @param address
     * @param email
     * @param loyal_points - punkty lojalnościowe
     */
    public Regular(String name, String surname, int code,
                      String address, String email,
                      int loyal_points){
        super(name, surname, code,
                address, email);
        this.loyal_points = loyal_points;
    }

    public int getLoyal_points() {
        return loyal_points;
    }

    public void setLoyal_points(int loyal_points) {
        this.loyal_points = loyal_points;
    }

    /**
     * Jeśli klient posiada co najmniej 100pkt może je
     * wykorzystać otrzymując zniżkę w wysokości 10%
     * wartości zakupów.
     */
    public boolean useLoyalPoints(){
        if (loyal_points>=100){
            //zniżka -10%
            loyal_points-=100;
            return true;
        }else{
            System.out.println("Niewystarczająca liczba punktów.");
            return false;
        }
    }

    @Override
    public BigInteger getAccount_number() {
        return null;
    }

    @Override
    public long getREGON() {
        return 0;
    }

    @Override
    public String getMessage_address() {
        return "";
    }
}
