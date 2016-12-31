package skeletor;

import skeletor.Enums.*;
import skeletor.Food.*;
import skeletor.Person.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jarek on 2016-12-30.
 */
public class RandomGenerator {

    private static volatile long orders;

    public RandomGenerator() {
        orders = 0;
    }

    //todo wyświetlanie stworzonej listy posiłków dla sprawdzenia poprawności generowania

    public void displayMeal(LinkedList<Meal> meals){
        for (Meal x : meals){
            System.out.print(x.getName()+"; ");
            String[] tmp = x.getComponents();
            for (int i = 0; i<tmp.length; i++){
                System.out.print(tmp[i]+ ", ");
            }
            System.out.print(x.getPrice()+"; "+x.getWeight()+"; "+x.getCategory()+"; "+x.getPreparation_time()+"; ");
            if (x.getName().substring(0,5).equals("pizza")){
                Pizza y = (Pizza)x;
                if (y.isSize_big()) {
                    System.out.print("Duża;");
                }else{
                    System.out.print("Mała;");
                }
            }
            if (x.getName().substring(0,6).equals("ciasto")){
                Cake y = (Cake) x;
                System.out.print(y.getCake_type()+";");
            }
            if (x.getName().substring(0,9).equals("naleśniki")){
                Pancake y = (Pancake) x;
                System.out.print(y.getAdditives()+";");
            }
            System.out.println();

        }

    }

    /**
     * Metoda generuje losowe posiłki i dodaje je na koniec listy wejsciowej.
     * @param meals lista posiłków
     */
    public void addRandomMeal (LinkedList<Meal> meals){
        Meal meal = null;
        Random random = new Random(System.currentTimeMillis());
        //tworzenie nazwy
        String nameMeal = generateMealName(random.nextInt(15)+10);
        //tworzenie listy składników
        int number_of_components = 0;
        number_of_components = random.nextInt(12)+3;
        String[] components = new String[number_of_components];
        for (int i =0; i<number_of_components; i++){
            String tmp = generateMealName(random.nextInt(15)+5);
            components[i] = tmp;
        }
        //tworzenie ceny
        int tmp_price = random.nextInt(30)+10;
        String tmp = tmp_price + ".99";
        BigDecimal price = new BigDecimal(tmp);
        System.out.println(price);
        //tworzenie wagi
        float weight = random.nextFloat()*10+1;
        //tworzenie czasu przygotowania
        long timePrepararation = random.nextInt(60)+1;

        E_KategoriaPosiłku category = null;
        int btmp = random.nextInt(3);
        switch (btmp){
            case 0:{
                category = E_KategoriaPosiłku.pizza;
                break;
            }
            case 1:{
                category = E_KategoriaPosiłku.makaron;
                break;
            }
            case 2:{
                category = E_KategoriaPosiłku.deser;
                break;
            }
        }

        switch (category){
            case pizza:{
                boolean pizza_size = random.nextBoolean();
                int choose = random.nextInt(3);
                switch (choose){
                    case 0:{
                        meal = new Margheritta("pizzam "+nameMeal, components, price, weight, category, timePrepararation, pizza_size);
                        break;
                    }
                    case 1:{
                        meal = new Hawaiian("pizzah "+nameMeal, components, price, weight, category, timePrepararation, pizza_size);
                        break;
                    }
                    case 2:{
                        meal = new Salami("pizzas "+nameMeal, components, price, weight, category, timePrepararation, pizza_size);
                        break;
                    }
                }
                break;
            }
            case makaron:{
                int choose = random.nextInt(2);
                switch (choose){
                    case 0:{
                        meal = new Spaghetti("spaghetti "+nameMeal, components, price, weight, category, timePrepararation);
                        break;
                    }
                    case 1:{
                        meal = new Lasagne("lasagne "+nameMeal, components, price, weight, category, timePrepararation);
                        break;
                    }
                }
                break;
            }
            case deser:{
                int choose = random.nextInt(2);
                switch (choose){
                    case 0:{
                        E_RodzajCiasta cake_type = null;
                        int c = random.nextInt(3);
                        switch (c){
                            case 0:{
                                cake_type = E_RodzajCiasta.Czekoladowe;
                                break;
                            }
                            case 1:{
                                cake_type = E_RodzajCiasta.Waniliowe;
                                break;
                            }
                            case 2:{
                                cake_type = E_RodzajCiasta.Truskawkowe;
                                break;
                            }
                        }
                        meal = new Cake("ciasto "+nameMeal,components,price,weight,category,timePrepararation,cake_type);
                        break;
                    }
                    case 1:{
                        E_Dodatki addition = null;
                        int c = random.nextInt(6);
                        switch (c) {
                            case 0: {
                                addition = E_Dodatki.jabłka;
                                break;
                            }
                            case 1: {
                                addition = E_Dodatki.dżem;
                                break;
                            }
                            case 2: {
                                addition = E_Dodatki.ser;
                                break;
                            }
                            case 3: {
                                addition = E_Dodatki.cukier;
                                break;
                            }
                            case 4: {
                                addition = E_Dodatki.czekolada;
                                break;
                            }
                            case 5: {
                                addition = E_Dodatki.truskawki;
                                break;
                            }
                        }
                        meal = new Pancake("naleśniki "+nameMeal,components,price,weight,category,timePrepararation,addition);
                        break;
                    }
                }
            }
        }
        if (meal != null) {
            meals.addLast(meal);
        }
    }

    private String generateMealName(int lenght){
        Random random = new Random(System.nanoTime());
        String name = "";
        for (int i = 0; i<lenght; i++){
            char sign = (char) (random.nextInt(57) + 65);
            name += sign;
        }
        return name;
    }

    /**
     * Metoda przydziela niepowtarzalne numery zamówienia.
     * @return - numer zamówienia
     */
    synchronized
    public long getOrdersNumber(){
        return orders++;
    }

    /**
     * Metoda wyświetla wszystkich dostawców z listy.
     * @param deliverers - lista dostawców
     */
    public void displayDeliverers(LinkedList<Deliverer> deliverers){
        for (Deliverer x : deliverers){
            System.out.print("Imię " + x.getName() + "; Nazwisko "+ x.getSurname() + "; PESEL "+ x.getPESEL() + "; dni pracy : " );
            E_Dni[] dni = x.getWork_day();
            for (E_Dni y : dni){
                System.out.print(y + " ");
            }
            System.out.print("; godziny pracy ");
            int[] hours = x.getWork_hour();
            for (int y : hours){
                System.out.print(y+ " ");
            }
            System.out.println("; uprawnienia "+ x.getCan_drive());
        }
    }

    /**
     * Metoda losuje wartości dla dostawcy , następnie tworzy jego obiekt i wsadza do listy na koniec.
     * @param deliverers - lista dostawców
     */
    public void addRandomDeliverer(LinkedList<Deliverer> deliverers){
        Random random = new Random(System.currentTimeMillis());

        //dane ogólne dla dostawcy
        String name = randomStringName(random.nextInt(10)+5);
        String surname = randomStringName(random.nextInt(24)+5);
        long numberPESEL = 0;
        do {
            numberPESEL = generatePESELNumber();
        }while (uniqPESELNumber(deliverers, numberPESEL));
        E_Dni[] daysWork = generateDaysWorkDeliverer();
        int[] hoursWork = generateHoursWordDeliverer();
        E_Uprawnienia uprawnienia;
        boolean type = random.nextBoolean();
        if (type) uprawnienia = E_Uprawnienia.samochód;
        else uprawnienia = E_Uprawnienia.skuter;

        Deliverer deliverer = new Deliverer(name,surname,numberPESEL,hoursWork,daysWork,uprawnienia);

        deliverers.addLast(deliverer);

    }

    /**
     * Metoda tworzy godziny pracy dostawcy. Zakładam, że dostawcy zależy na pracy na pełny etat
     * , a jego "godziny dostępności" są zależne od umowy o pracę.
     * @return - zwraca posortowaną tablicę godzin pracy dostawcy
     */
    private int[] generateHoursWordDeliverer(){
        Random random = new Random(System.currentTimeMillis());
        int[] hoursWork = new int[8];
        for (int x : hoursWork) x = -1;
        while (checkFillHoursWorkArray(hoursWork)){
            int position = random.nextInt(8);
            int hours = random.nextInt(24);
            hoursWork[position] = hours;
        }

        for (int i = 0; i<8; i++){
            for (int k = 0; k<8; k++){
                if (hoursWork[k]>hoursWork[i]) {
                    int tmp = hoursWork[k];
                    hoursWork[k] = hoursWork[i];
                    hoursWork[i] = tmp;
                }
            }
        }

        return hoursWork;
    }

    /**
     * Metoda sprawdza zapełnienie i unikalność danych w tablicy godzin pracy dostawcy
     * @param hours - tablica godzin pracy
     * @return - true jeśli tablica jest niezapełniona lub jeśli godziny pracy się powtarzają, false jeśli tablica jest ok
     */
    private boolean checkFillHoursWorkArray(int[] hours){
        for (int i = 0; i<hours.length; i++){
            if (hours[i] == -1) return true;
            for (int k = 0; k<hours.length; k++){
                if (k != i && hours[k] == hours[i]) return true;
            }
        }
        return false;
    }

    /**
     * Metoda tworzy dni pracy dostawcy. Zakładam, że dostawca może pracować tylko 4 dni w tygodniu, taka umowa.
     * @return - zwraca tablicę dni pracy dostawcy
     */
    private E_Dni[] generateDaysWorkDeliverer(){
        Random random = new Random(System.currentTimeMillis());
        E_Dni[] tmp = new E_Dni[4];
        while(checkFillDaysWorkArray(tmp)){
            int position = random.nextInt(4);
            int day = random.nextInt(7);
            switch (day){
                case 0:{
                    tmp[position] = E_Dni.poniedziałek;
                    break;
                }case 1:{
                    tmp[position] = E_Dni.wtorek;
                    break;
                }case 2:{
                    tmp[position] = E_Dni.środa;
                    break;
                }case 3:{
                    tmp[position] = E_Dni.czwartek;
                    break;
                }case 4:{
                    tmp[position] = E_Dni.piątek;
                    break;
                }case 5:{
                    tmp[position] = E_Dni.sobota;
                    break;
                }case 6:{
                    tmp[position] = E_Dni.niedziela;
                    break;
                }
            }
        }

        return tmp;
    }

    /**
     * Metoda sprawdza zapełnienie i unikalność danych w tablicy dni pracy dostawcy
     * @param e_dni- tablica dni pracy
     * @return - true jeśli tablica jest niezapełniona lub jeśli dni pracy się powtarzają, false jeśli tablica jest ok
     */
    private boolean checkFillDaysWorkArray(E_Dni[] e_dni){
        for (int i = 0 ; i<e_dni.length; i++){
            if (e_dni[i] == null) return true;
            for (int k = 0; k<e_dni.length; k++){
                if (k != i && e_dni[k] == e_dni[i]) return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza unikalność numeru PESEL dostawcy.
     * @param deliverers - lista dostawców
     * @param newPESEL - numer PESEL do sprawdzenia
     * @return - true jeśli PESEL już wystąpił, false jeśli PESEL jest unikalny
     */
    private boolean uniqPESELNumber(LinkedList<Deliverer> deliverers, long newPESEL){
        if (deliverers.size() != 0){
            for (Deliverer x : deliverers){
                if (x.getPESEL() == newPESEL) return true;
            }
        }
        return false;
    }

    /**
     * Metoda generuje losowy numer PESEL, nie sprawdzam żadnego kryterium numeru PESEL, wygenerowana liczba z rzeczywistym
     * numerem PESEL ma wspólną tylko długość (11 liczb)
     * @return
     */
    private long generatePESELNumber(){
        Random random = new Random(System.currentTimeMillis());
        long tmp = random.nextInt(9)+1;
        for (int i  =1; i<11; i++){
            tmp = tmp*10 + random.nextInt(10);
        }
        return tmp;
    }

    /**
     * Metoda wyświetla wsyzstkich klientów na liście.
     * @param clients - lista klientów
     */
    public void displayClient(LinkedList<Client> clients){
        for (Client x : clients){
            System.out.println("Imię " + x.getName() + "; Nazwisko "+ x.getSurname() + "; clientID "+ x.getCode()
                    + "; czas zamówienia "+ x.getTime_order() + "; adres "+ x.getAddress() + "; mail "+ x.getEmail()
                    + "; punkty lojalnościowe "+ x.getLoyal_points() + "; adres korespondencyjny "+ x.getMessage_address()
                    + "; REGON "+ x.getREGON() + "; numer konta "+ x.getAccount_number());
        }
    }

    /**
     * Metoda sprawdza unikalność numeru REGON.
     * @param clients - lista klientów
     * @param REGON - nowy numer do sprawdzenia
     * @return - true jeśli już wystąpił, false jeśli jest unikalny
     */
    private boolean checkUniqREGON(LinkedList<Client> clients, long REGON){
        if (clients.size() != 0) {
            for (Client x : clients) {
                if (x.getREGON() == REGON) return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza unikalność numeru konta.
     * @param clients - lista klientów
     * @param accountNumber - nowy numer do sprawdzenia
     * @return - true jeśli już wystąpił, false jeśli jest unikalny
     */
    private boolean checkUniqAccountNumber(LinkedList<Client> clients, String accountNumber){
        if (clients.size()!=0) {
            BigInteger tmp = new BigInteger(accountNumber);
            for (Client x : clients) {
                if (x.getAccount_number() != null && x.getAccount_number().compareTo(tmp) == 0) return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza unikalność numeru identyfikacji klienta.
     * @param clients - lista klientów
     * @param newClientID - nowy numer do sprawdzenia
     * @return - true jeśli już wystąpił, false jeśli jest unikalny
     */
    private boolean checkUniqClientID(LinkedList<Client> clients, int newClientID){
        if (clients.size() != 0) {
            for (Client x : clients) {
                if (x.getCode() == newClientID) return true;
            }
        }
        return false;
    }

    /**
     * Tworzenie nowego klienta.
     * @param clients_list - lista istniejących klientów
     */
    public void addRandomClient(LinkedList<Client> clients_list){
        Random random = new Random(System.currentTimeMillis());

        //dane ogólne dla każdego klienta
        String name = randomStringName(random.nextInt(10)+5);
        String surname = randomStringName(random.nextInt(24)+5);
        int clientPersonalIDNumber = 0;
        do {
            clientPersonalIDNumber = getPrivateNumberOfOrdered();
        } while (checkUniqClientID(clients_list, clientPersonalIDNumber));

        String address = generateAddress();

        int client_type = random.nextInt(3);
        switch (client_type){
            case 0:{
                String mailAddress = randomStringName(random.nextInt(17)+5);
                Occasional client = new Occasional(name, surname, clientPersonalIDNumber
                        , address, mailAddress);
                clients_list.addLast(client);
                break;
            }
            case 1:{
                String mailAddress = randomStringName(random.nextInt(17)+5);
                Regular client = new Regular(name, surname, clientPersonalIDNumber
                        , address, mailAddress, 0);
                clients_list.addLast(client);
                break;
            }
            case 2:{
                String messageAddress = randomStringName(random.nextInt(20)+10);
                String accountStringNumber = "";
                do {
                    accountStringNumber = accountNumberGenerator(); // dodać sprawdzenie unikalności
                }while (checkUniqAccountNumber(clients_list, accountStringNumber));
                long numberREGON = 0;
                do {
                    numberREGON = generatorRegonNumber(); // dodać sprawdzenie unikalności
                }while (checkUniqREGON(clients_list, numberREGON));
                Corporate client = new Corporate(name, surname,clientPersonalIDNumber
                        , address,messageAddress,accountStringNumber,numberREGON);
                clients_list.addLast(client);
                break;
            }
        }

    }

    /**
     * Metoda generująca numer Regon, 9 lub 14 cyfrowy
     * @return - numer regon
     */
    private long generatorRegonNumber(){
        Random random = new Random(System.currentTimeMillis());
        boolean option = random.nextBoolean();
        if (option){
            long regon = random.nextInt(9)+1;
            for (int i =1; i<9; i++){
                regon = regon*10+ random.nextInt(9);
            }
            return regon;
        }else {
            long regon = random.nextInt(9)+1;
            for (int i =1; i<14; i++){
                regon = regon*10+ random.nextInt(9);
            }
            return regon;
        }
    }

    /**
     * Metoda tworzy 26 cyfrowy numer konta.
     * @return - numer konta
     */
    private String accountNumberGenerator(){
        Random random = new Random(System.currentTimeMillis());
        String account = "";
        account += (random.nextInt(9)+1);
        for (int i = 1; i<26; i++ ){
            account += (random.nextInt(10));
        }
        return account;
    }

    /**
     * Metoda generuje adres na mapie.
     * @return - współrzędne x i y na macierzy, zapisane w postaci "x:y"
     */
    private String generateAddress(){
        Random random = new Random(System.currentTimeMillis());
        int x = random.nextInt(900);
        int y = random.nextInt(750);
        return (x +":"+y);
    }

    /**
     * Metoda generuje maksymalnie 6 cyfrowy numer identyfikujący klienta.
     * @return - numer identyfikacji klienta
     */
    private int getPrivateNumberOfOrdered(){
        int code = 0;
        Random random = new Random(System.currentTimeMillis());
        for (int i =0; i<6; i++){
            code=code*10+random.nextInt(10);
        }
        return code;
    }

    /**
     * Metoda tworzy losowy ciąg znaków o podanej długości , przedział znaków od 65 do 122 znaku tablicy ASCII.
     * @param lenght - długość
     * @return - losowy ciąg znaków
     */
    private String randomStringName(int lenght){
        Random random = new Random(System.currentTimeMillis());
        String string = "";
        for (int i = 0 ; i<lenght; i++){
            char sign = (char)(random.nextInt(57) + 65);
            string+= sign;
        }
        return string;
    }

}
