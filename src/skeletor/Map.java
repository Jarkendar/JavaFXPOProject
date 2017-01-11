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

    /**
     * Konstruktor mapy, wraz z ustawieniem pozycji restauracji
     * @param width wysokość mapy
     * @param lenght szerogość mapy
     * @param wRestaurant współrzędna X restauracji
     * @param lRestaurant współrzędna Y restauracji
     */
    public Map(int width, int lenght, int wRestaurant, int lRestaurant) {
        this.width = width;
        this.lenght = lenght;
        this.map = new int[width][lenght];
        map[wRestaurant][lRestaurant] = 1;
    }

    /**
     * Metoda dodaje klientów na mapie.
     * @param clients lista klientów
     */
    public void addClientToMap(LinkedList<Client> clients){
        for (Client x: clients) {
            String address = x.getAddress();
            String[] coordinats = address.split(":");
            int width = Integer.parseInt(coordinats[0]);
            int lenght = Integer.parseInt(coordinats[1]);
            map[width][lenght] = 2;
        }
    }

    public boolean setDelivererPositionOnMap(int oldX, int oldY, int posX, int posY){
        synchronized (this) {
            if (map[posX][posY] == 0) {
                if (map[oldX][oldY] != 1) {
                    map[oldX][oldY] = 0;
                }
                map[posX][posY] = 3;
                displayMap();
                return true;
            } else if (map[posX][posY] == 2) {
                if (oldX < posX){
                    posX--;
                }else if (oldX > posX){
                    posX++;
                }else if (oldY < posY){
                    posY--;
                }else if (oldY > posY){
                    posY++;
                }
                if (map[oldX][oldY] == 1) {
                    map[oldX][oldY] = 0;
                }
                map[posX][posY] = 3;
                displayMap();
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * Wyświetlenie mapy.
     */
    public void displayMap(){
        for (int x[]: map){
            for(int y: x){
                System.out.print(y+" ");
            }
            System.out.println();
        }
    }
}
