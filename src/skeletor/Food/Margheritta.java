package skeletor.Food;

import skeletor.Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Margheritta extends Pizza {

    /**
     * Konstrukrot klasy Food.Margheritta Food.Pizza
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     * @param size_big
     */
    public Margheritta(String name, String[] components,
                       double price, float weight,
                       E_KategoriaPosiłku category,
                       long preparation_time, boolean size_big) {
        super(name, components, price, weight,
                category, preparation_time,
                size_big);
    }
}
