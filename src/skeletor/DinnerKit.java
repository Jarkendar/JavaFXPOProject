package skeletor;

import skeletor.Food.*;

import java.math.BigDecimal;

/**
 * Created by Jarek on 2016-12-02.
 */
public class DinnerKit {
    private byte kit_number;
    private Meal[] meal_list;

    /**
     * Konstruktor klasy DinnerKit
     *
     * @param kit_number - numer zestawu
     * @param meal_list  - lista dań zestawu
     */
    public DinnerKit(byte kit_number, Meal[] meal_list) {
        this.setKit_number(kit_number);
        this.setMeal_list(meal_list);
    }

    /**
     * Metoda oblicza cenę zestawu na podstawie cen posiłków
     *
     * @return - zwraca cenę całego zestawu
     */
    public BigDecimal calculateKitPrice() {
        BigDecimal price = new BigDecimal(0);
        for (Meal x : this.meal_list) {
            price = price.add(x.getPrice());
        }
        return price;
    }

    /**
     * Metoda oblicza wagę zestawu na podstawie wag posiłków
     *
     * @return - zwraca wagę całego zestawu
     */
    public float calculateKitWeight() {
        float weight = 0;
        for (Meal x : this.meal_list) {
            weight += x.getWeight();
        }
        return weight;
    }

    /**
     * Metoda wyświetlająca nazwy posiłków z zestawu.
     */
    public void displayNameMeals() {
        for (Meal x : meal_list) {
            System.out.println(x.getName() + " ");
        }
    }

    /**
     * Getter
     *
     * @return numer zestawu
     */
    public byte getKit_number() {
        return kit_number;
    }

    public void setKit_number(byte kit_number) {
        this.kit_number = kit_number;
    }

    public Meal[] getMeal_list() {
        return meal_list;
    }

    public void setMeal_list(Meal[] meal_list) {
        this.meal_list = meal_list;
    }
}
