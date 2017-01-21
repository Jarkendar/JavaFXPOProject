package skeletor.Person;

import java.math.BigInteger;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Corporate extends Client {

    private String message_address;
    private BigInteger account_number;
    private long REGON;


    /**
     * Konstruktor klasy Corporate Client z adresem email
     * @param name
     * @param surname
     * @param code
     * @param address
     * @param message_address - adres korespondencyjny
     * @param account_string - string z 26 cyfrowym numerem konta
     * @param REGON - 9 lub 14 cyfrowy kod REGON
     */
    public Corporate(String name, String surname, int code,
                     String address, String message_address,
                     String account_string, long REGON){
        super(name, surname, code,
                address);
        this.message_address = message_address;
        this.account_number = new BigInteger(account_string);
        this.REGON = REGON;
    }

//    /**
//     * Konstruktor klasy Corporate Client z adresem email
//     * @param name
//     * @param surname
//     * @param code
//     * @param address
//     * @param email
//     * @param message_address - adres korespondencyjny
//     * @param account_string - string z 26 cyfrowym numerem konta
//     * @param REGON - 9 lub 14 cyfrowy kod REGON
//     */
//    public Corporate(String name, String surname, int code,
//                      String address, String email,
//                      String message_address, String account_string,
//                     long REGON){
//        super(name, surname, code,
//                address, email);
//        this.message_address = message_address;
//        this.account_number = new BigInteger(account_string);
//        this.REGON = REGON;
//    }

    public String getMessage_address() {
        return message_address;
    }

    public void setMessage_address(String message_address) {
        this.message_address = message_address;
    }

    public BigInteger getAccount_number() {
        return account_number;
    }

    public long getREGON() {
        return REGON;
    }

    public void setREGON(long REGON) {
        this.REGON = REGON;
    }

    @Override
    public int getLoyal_points() {
        return 0;
    }
}
