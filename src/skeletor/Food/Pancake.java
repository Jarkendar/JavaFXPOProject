package skeletor.Food;

import skeletor.Enums.E_Dodatki;
import skeletor.Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Pancake extends Dessert {
    private E_Dodatki additives;

    /**
     * Konstruktor klasy Food.Pancake Food.Dessert
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     * @param additives - dodatki do naleśników
     */
    public Pancake(String name, String[] components,
                   double price, float weight,
                   E_KategoriaPosiłku category,
                   long preparation_time, E_Dodatki additives) {
        super(name, components, price, weight,
                category, preparation_time);
        this.additives = additives;
    }

    public E_Dodatki getAdditives() {
        return additives;
    }

    public void setAdditives(E_Dodatki additives) {
        this.additives = additives;
    }
}
