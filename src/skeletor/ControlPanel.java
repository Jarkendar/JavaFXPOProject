package skeletor;


import skeletor.Food.Meal;
import skeletor.Person.*;

import java.util.LinkedList;

import static java.lang.Thread.sleep;

/**
 * Created by Jarek on 2016-12-02.
 */
public class ControlPanel {

    public static void main(String[] args) {
        LinkedList<Client> clients_list = new LinkedList<>();
        LinkedList<Deliverer> deliverers_list = new LinkedList<>();
        LinkedList<Meal> meals_list = new LinkedList<>();
        RandomGenerator randomGenerator = new RandomGenerator();


        for (int i = 0 ; i<10; i++){
            randomGenerator.addRandomClient(clients_list);

            //zabezpieczenie przed losowanie zmiennych o tym samym seedzie randoma
            try {
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }

        for (int i = 0; i<10; i++){
            randomGenerator.addRandomDeliverer(deliverers_list);
            try{
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }

        for (int i = 0; i<10; i++){
            randomGenerator.addRandomMeal(meals_list);
            try{
                sleep(1);
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }


        randomGenerator.displayDeliverers(deliverers_list);
        randomGenerator.displayClient(clients_list);

    }


}
