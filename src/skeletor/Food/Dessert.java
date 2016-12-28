package skeletor.Food;

import Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Dessert extends Meal {

    /**
     * Konstruktor klasy Food.Dessert Food.Meal
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     */
    public Dessert(String name, String[] components,
                   double price, float weight,
                   E_KategoriaPosiłku category,
                   long preparation_time) {
        super(name, components, price, weight,
                category, preparation_time);
    }
}
