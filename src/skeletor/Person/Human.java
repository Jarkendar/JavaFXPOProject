package skeletor.Person;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Human {

    private String name;
    private String surname;

    /**
     * Konstruktor klasy Human
     * @param name -imię
     * @param surname - nazwisko
     */
    public Human(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
