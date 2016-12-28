package skeletor.Food;

import Enums.E_KategoriaPosiłku;
import Enums.E_RodzajCiasta;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Cake extends Dessert {
    private E_RodzajCiasta cake_type;

    /**
     * Konstruktor klasy Food.Cake Food.Dessert
     * @param name
     * @param components
     * @param price
     * @param weight
     * @param category
     * @param preparation_time
     * @param cake_type - rodzaj ciasta
     */
    public Cake(String name, String[] components,
                double price, float weight,
                E_KategoriaPosiłku category,
                long preparation_time,
                E_RodzajCiasta cake_type) {
        super(name, components, price,
                weight, category, preparation_time);
        this.cake_type = cake_type;
    }

    public E_RodzajCiasta getCake_type() {
        return cake_type;
    }

    public void setCake_type(E_RodzajCiasta cake_type) {
        this.cake_type = cake_type;
    }
}
