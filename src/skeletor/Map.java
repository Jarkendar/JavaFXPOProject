package skeletor;

import skeletor.Person.Client;

import java.util.LinkedList;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Map {
    private int width;
    private int lenght;
    private int[][] map;

    public Map(int width, int lenght, int wRestaurant, int lRestaurant) {
        this.width = width;
        this.lenght = lenght;
        this.map = new int[width][lenght];
        for (int i = 0; i<map.length; i++){
            for(int j = 0; j<map[i].length; j++){
                map[j][i] = 0;
            }
        }
        map[lRestaurant][wRestaurant] = 1;
    }

    public void addClientToMap(LinkedList<Client> clients){
        for (Client x: clients) {
            String address = x.getAddress();
            String[] coordinats = address.split(":");
            int width = Integer.parseInt(coordinats[0]);
            int lenght = Integer.parseInt(coordinats[1]);
            map[lenght][width] = 2;
        }
    }

    public void displayMap(){
        for (int x[]: map){
            for(int y: x){
                System.out.print(y+" ");
            }
            System.out.println();
        }
    }
}
