package skeletor.Person;

import java.math.BigInteger;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Occasional extends Client {

    /**
     * Konstruktor klasy Occasional Client bez adresu email
     * @param name
     * @param surname
     * @param code
     * @param address
     */
    public Occasional(String name, String surname, int code,
                      String address){
        super(name, surname, code,
                address);
    }

    /**
     * Konstruktor klasy Occasional Client z adresem email
     * @param name
     * @param surname
     * @param code
     * @param address
     * @param email
     */
    public Occasional(String name, String surname, int code,
                      String address, String email){
        super(name, surname, code,
                address, email);
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
    public int getLoyal_points() {
        return 0;
    }

    @Override
    public String getMessage_address() {
        return "";
    }
}
