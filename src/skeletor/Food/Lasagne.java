package skeletor.Food;

import skeletor.Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Lasagne extends Noodle {

    /**
     * Konstruktor klasy Food.Lasagne Food.Noodle
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     */
    public Lasagne(String name, String[] components,
                   double price, float weight,
                   E_KategoriaPosiłku category,
                   long preparation_time) {
        super(name, components, price, weight,
                category, preparation_time);
    }
}
