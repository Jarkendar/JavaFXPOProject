package sample;

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

    public void addClient(MouseEvent mouseEvent) {
        label_Count_Client.setText(Integer.toString(Integer.parseInt(label_Count_Client.getText())+1));
    }

    public void delClient(MouseEvent mouseEvent) {
        if (Integer.parseInt(label_Count_Client.getText()) != 0){
            label_Count_Client.setText(Integer.toString(Integer.parseInt(label_Count_Client.getText())-1));
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
