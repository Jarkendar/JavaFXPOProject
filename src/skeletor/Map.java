package skeletor;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import packetGUI.Main;
import skeletor.Person.Client;

import java.util.LinkedList;

/**
 * Created by Jarek on 2016-12-02.
 */
public class Map {
    private int width;
    private int lenght;
    private int[][] map;
    private Button[][] mapGUI;

    /**
     * Konstruktor mapy, wraz z ustawieniem pozycji restauracji
     *
     * @param width       wysokość mapy
     * @param lenght      szerogość mapy
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
     * Ustawianie macierzy przycisków do mapy
     * @param mapGUI macierzy przycisków
     */
    synchronized
    public void setMapGUI(Button[][] mapGUI) {
        this.mapGUI = mapGUI;
        setRestaurantOnMap();
    }

    synchronized
    public void setClientOnMap(LinkedList<Client> clientLinkedList){
        for (int i = 0; i<Main.getWidth(); i++){
            for (int k = 0; k<Main.getLenght(); k++){
                mapGUI[i][k].setText("0");
                mapGUI[i][k].setVisible(false);
                mapGUI[i][k].setStyle("-fx-background-color: #fff;");
            }
        }

        setRestaurantOnMap();

        for (Client x : clientLinkedList) {
            String address = x.getAddress();
            String[] coordinats = address.split(":");
            int width = Integer.parseInt(coordinats[0]);
            int lenght = Integer.parseInt(coordinats[1]);
            mapGUI[width][lenght].setText("K");
            mapGUI[width][lenght].setVisible(true);
            mapGUI[width][lenght].setTextFill(Color.WHITE);
            mapGUI[width][lenght].setStyle("-fx-background-color: #00CC00;");
        }
    }

    /**
     * Metoda ustawia formatowanie przycisku symbolizującego restaurację.
     */
    synchronized
    private void setRestaurantOnMap() {
        mapGUI[Main.getwRestaurant()][Main.getlRestaurant()].setVisible(true);
        mapGUI[Main.getwRestaurant()][Main.getlRestaurant()].setTextFill(Color.WHITE);//ustawienie koloru wypełnienia
        mapGUI[Main.getwRestaurant()][Main.getlRestaurant()].setText("R");//ustawienie etykiety przycisku
        mapGUI[Main.getwRestaurant()][Main.getlRestaurant()].setStyle("-fx-background-color: #990000;");//ustawienie czerwonego tła przycisku

    }

    /**
     * Metoda dodaje klientów na mapie.
     *
     * @param clients lista klientów
     */
    public void addClientToMap(LinkedList<Client> clients) {
        for (Client x : clients) {
            String address = x.getAddress();
            String[] coordinats = address.split(":");
            int width = Integer.parseInt(coordinats[0]);
            int lenght = Integer.parseInt(coordinats[1]);
            map[width][lenght] = 2;
            if (width - 1 >= 0) {
                map[width - 1][lenght] = 0;
            }
            if (width + 1 < map.length) {
                map[width + 1][lenght] = 0;
            }
            if (lenght - 1 >= 0) {
                map[width][lenght - 1] = 0;
            }
            if (lenght + 1 < map[width].length) {
                map[width][lenght + 1] = 0;
            }
            displayMap();
        }
    }

    /**
     * Metoda ustawia na mapie znacznik dostawcy. Wykonywać tę metodę może maksymalnie
     * jeden dostawca w tym samym czasie. Stare współrzędne w macierzy w przypadku
     * możliwości wykonania ruchu są ustawiane na 0;
     *
     * @param oldX     stary rząd położenia dostawcy
     * @param oldY     stara kolumna położenia dostawcy
     * @param posX     nowy rząd położenia dostawcy
     * @param posY     nowa kolumna położenia dostawcy
     * @param guardian obiekt strażnika pilnującego synchronizacji
     * @return true - dostawca może się poruszyć na wskazane pole, false - jeśli pole docelowe jest zajęte
     */
    synchronized
    public boolean setDelivererPositionOnMap(int oldX, int oldY, int posX, int posY, Object guardian) {
        synchronized (guardian) {
            if (map[posX][posY] == 0) {
                if (map[oldX][oldY] != 1 && map[oldX][oldY] != 2) {
                    map[oldX][oldY] = 0;
                }
                map[posX][posY] = 3;
                displayMap();
                return true;
            } else if (map[posX][posY] == 2) {
                if (oldX < posX) {
                    posX--;
                } else if (oldX > posX) {
                    posX++;
                } else if (oldY < posY) {
                    posY--;
                } else if (oldY > posY) {
                    posY++;
                }
                if (map[oldX][oldY] != 1 && map[oldX][oldY] != 2) {
                    map[oldX][oldY] = 0;
                }
                map[posX][posY] = 3;
                displayMap();
                return true;
            } else if (map[posX][posY] == 1) {
                if (map[oldX][oldY] != 1 && map[oldX][oldY] != 2) {
                    map[oldX][oldY] = 0;
                    displayMap();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Wyświetlenie mapy.
     */
    public void displayMap() {
        for (int x[] : map) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}
