package packetGUI;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Controller {

    public Button button_Del_Client;
    public Button button_Add_Client;
    public Button button_Add_Deliverer;
    public Button button_Del_Deliverer;
    public Button button_Add_Meal;
    public Button button_Order;

    public javafx.scene.control.Label label_Client;
    public javafx.scene.control.Label label_Deliverer;
    public javafx.scene.control.Label label_Meal;
    public javafx.scene.control.Label label_Count_Meal;
    public javafx.scene.control.Label label_Count_Client;
    public javafx.scene.control.Label label_Count_Deliverer;

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

    public void delClient(MouseEvent mouseEvent) {
        synchronized (this) {
            if (Main.canOrderDeleteClient()) {
                Main.setClientToNotExist();
            }
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
        Main.addMealToMealList();
        label_Count_Meal.setText(Integer.toString(Main.getMeals_list().size()));
    }

    public void openOrderView(MouseEvent mouseEvent) {
    }
}
