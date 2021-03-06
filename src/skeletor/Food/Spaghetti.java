package skeletor.Food;

import skeletor.Enums.E_KategoriaPosiłku;

import java.math.BigDecimal;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Spaghetti extends Noodle {

    /**
     * Konstruktor klasy Spaghetti Food.Noodle
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     */
    public Spaghetti(String name, String[] components,
                     BigDecimal price, float weight,
                     E_KategoriaPosiłku category,
                     long preparation_time) {
        super(name, components, price, weight,
                category, preparation_time);
    }
}
