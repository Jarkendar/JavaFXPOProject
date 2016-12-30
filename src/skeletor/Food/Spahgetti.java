package skeletor.Food;

import skeletor.Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Spahgetti extends Noodle {

    /**
     * Konstruktor klasy Spaghetti Food.Noodle
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     */
    public Spahgetti(String name, String[] components,
                     double price, float weight,
                     E_KategoriaPosiłku category,
                     long preparation_time) {
        super(name, components, price, weight,
                category, preparation_time);
    }
}
