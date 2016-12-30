package skeletor.Food;

import skeletor.Enums.E_KategoriaPosiłku;

/**
 * Created by Jarek on 2016-12-02.
 */
public abstract class Meal {
    private String name;
    private String[] components;
    private double price;
    private float weight;
    private E_KategoriaPosiłku category;
    private long preparation_time;

    /**
     * Konstruktor klasy Food.Meal
     * @param name - nazwa posiłku
     * @param components - lista składników
     * @param price - cena posiłku
     * @param weight - waga posiłku
     * @param category - kategoria posiłku
     * @param preparation_time - czas przygotowania
     */
    public Meal(String name, String[] components,
                double price, float weight,
                E_KategoriaPosiłku category,
                long preparation_time) {
        this.name = name;
        this.components = components;
        this.price = price;
        this.weight = weight;
        this.category = category;
        this.preparation_time = preparation_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public E_KategoriaPosiłku getCategory() {
        return category;
    }

    public void setCategory(E_KategoriaPosiłku category) {
        this.category = category;
    }

    public long getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(long preparation_time) {
        this.preparation_time = preparation_time;
    }
}
