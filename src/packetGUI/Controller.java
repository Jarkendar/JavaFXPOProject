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

    /**
     * Dodanie klienta.
     * @param mouseEvent
     */
    public void addClient(MouseEvent mouseEvent) {
        mouseEvent.getSource();//wyciąganie elementu na którym została wykonana akcji
        if (Main.getSizeOfClientList() < 40) {
            Main.addClientToList();
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()));
            button_Del_Client.setDisable(false);
        }else {
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()) + " osiągnięto limit klientów." );
        }
    }

    /**
     * Usunięcie klienta.
     * @param mouseEvent
     */
    public void delClient(MouseEvent mouseEvent) {
        synchronized (this) {
            if (Main.canOrderDeleteClient()) {
                Main.setClientToNotExist();
            }else {
                button_Del_Client.setDisable(true);
            }
            if(Integer.parseInt(label_Count_Client.getText()) != 0) {
                label_Count_Client.setText(Integer.toString(Integer.parseInt(label_Count_Client.getText()) - 1));
            }
            if (Main.getSizeOfClientList() == 0) {
                label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()) + " brak klientów.");
            }
        }
    }

    /**
     * Dodanie dostawcy.
     * @param mouseEvent
     */
    public void addDeliverer(MouseEvent mouseEvent) {
        if (Main.getSizeOfDelivererList() < 40) {
            Main.addDelivererToList();
            label_Count_Deliverer.setText(Integer.toString(Main.getSizeOfDelivererList()));
            button_Del_Deliverer.setDisable(false);
        }else {
            label_Count_Deliverer.setText(Integer.toString(Main.getSizeOfDelivererList()) + " osiągnięto limit dostawców." );
        }
    }

    /**
     * Usunięcie dostawcy.
     * @param mouseEvent
     */
    public void delDeliverer(MouseEvent mouseEvent) {
        if (Integer.parseInt(label_Count_Deliverer.getText()) != 0){
            label_Count_Deliverer.setText(Integer.toString(Integer.parseInt(label_Count_Deliverer.getText())-1));
        }
    }

    /**
     * Dodanie posiłku.
     * @param mouseEvent
     */
    public void addMeal(MouseEvent mouseEvent) {
        Main.addMealToMealList();
        label_Count_Meal.setText(Integer.toString(Main.getMeals_list().size()));
    }

    public void openOrderView(MouseEvent mouseEvent) {
    }
}
