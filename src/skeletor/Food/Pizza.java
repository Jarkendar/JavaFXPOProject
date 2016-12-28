package skeletor.Food;

import Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Pizza extends Meal {
    private boolean size_big;

    /**
     * Konstruktor klasy Food.Pizza Food.Meal
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     * @param size_big - rozmiar true=duża, false=mała
     */
    public Pizza(String name, String[] components,
                 double price, float weight,
                 E_KategoriaPosiłku category,
                 long preparation_time, boolean size_big) {
        super(name, components, price,
                weight, category, preparation_time);
        this.size_big = size_big;
    }

    public boolean isSize_big() {
        return size_big;
    }

    public void setSize_big(boolean size_big) {
        this.size_big = size_big;
    }
}
