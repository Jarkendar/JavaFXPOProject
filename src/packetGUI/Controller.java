package packetGUI;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Controller {

    public Button button_Del_Client;
    public Button button_Add_Client;
    public Button button_Add_Deliverer;
    public Button button_Del_Deliverer;
    public Button button_Add_Meal;
    public Button button_Del_Meal;
    public Button button_Order;

    public javafx.scene.control.Label label_Client;
    public javafx.scene.control.Label label_Deliverer;
    public javafx.scene.control.Label label_Meal;
    public javafx.scene.control.Label label_Count_Meal;
    public javafx.scene.control.Label label_Count_Client;
    public javafx.scene.control.Label label_Count_Deliverer;


    private volatile int countOfDel = 0;


    public void setCountOfDel() {
        countOfDel--;
    }

    public int getCountOfDel() {
        return countOfDel;
    }
    /**
     * Tutaj inicjalizuje się wszystkie zmienne.
     */
    public void initialize(){
     //   EventHandler handler = new EventHandler(){};//tworzenie uchwytu dla akcji

    }

    public void addClient(MouseEvent mouseEvent) {
        mouseEvent.getSource();//wyciąganie elementu na którym została wykonana akcji
        if (Main.getSizeOfClientList() < 40) {
            Main.addClientToList();
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()));
        }else {
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()) + " osiągnięto limit klientów." );
        }
    }

    public void setLabel_Meal(int countMeal){
        if (label_Count_Meal != null)
        label_Count_Meal.setText(Integer.toString(countMeal));
    }

    public void delClient(MouseEvent mouseEvent) {
        synchronized (this) {
            Main.setClientToNotExist();
            if(Integer.parseInt(label_Count_Client.getText()) != 0) {
                label_Count_Client.setText(Integer.toString(Integer.parseInt(label_Count_Client.getText()) - 1));
            }
            if (Main.getSizeOfClientList() == 0) {
                label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()) + " brak klientów.");
            }
        }
    }

    public void addDeliverer(MouseEvent mouseEvent) {
        label_Count_Deliverer.setText(Integer.toString(Integer.parseInt(label_Count_Deliverer.getText())+1));
    }

    public void delDeliverer(MouseEvent mouseEvent) {
        if (Integer.parseInt(label_Count_Deliverer.getText()) != 0){
            label_Count_Deliverer.setText(Integer.toString(Integer.parseInt(label_Count_Deliverer.getText())-1));
        }
    }

    public void addMeal(MouseEvent mouseEvent) {
        label_Count_Meal.setText(Integer.toString(Integer.parseInt(label_Count_Meal.getText())+1));
    }

    public void delMeal(MouseEvent mouseEvent) {
        if (Integer.parseInt(label_Count_Meal.getText()) != 0){
            label_Count_Meal.setText(Integer.toString(Integer.parseInt(label_Count_Meal.getText())-1));
        }
    }

    public void openOrderView(MouseEvent mouseEvent) {
    }
}
