package skeletor;


import skeletor.Food.Meal;
import skeletor.Person.*;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Jarek on 2016-12-02.
 */
public class ControlPanel {

    public static void main(String[] args) {
        Object guardian = new Object();

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
        Random random = new Random(System.currentTimeMillis());
        DinnerKit[] dinnerKits = new DinnerKit[10];
        for (int i = 0; i<10; i++){
            int count_of_meal = random.nextInt(4)+1;
            Meal[] meals_in_DinnerKit = new Meal[count_of_meal];
            for (int k = 0; k<count_of_meal; k++){
                try{
                    sleep(1);
                }catch (InterruptedException e){
                    System.out.println(e);
                }
                int number_of_meal = random.nextInt(meals_list.size());
                meals_in_DinnerKit[k] = meals_list.get(number_of_meal);
            }
            dinnerKits[i] = new DinnerKit((byte)(i+1),meals_in_DinnerKit);
        }

        for (DinnerKit x: dinnerKits){
            System.out.println(x.getKit_number());
            System.out.println(x.calculateKitPrice());
            System.out.println(x.calculateKitWeight());
            x.displayNameMeals();
        }

//        randomGenerator.displayDeliverers(deliverers_list);
//        randomGenerator.displayClient(clients_list);
        randomGenerator.displayMeal(meals_list);

    }


}
