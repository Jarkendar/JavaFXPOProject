package packetGUI;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Controller {

    public Button button_Del_Client;
    public Button button_Add_Client;
    public Button button_Add_Deliverer;
    public Button button_Del_Deliverer;
    public Button button_Add_Meal;


    public javafx.scene.control.Label label_Client;
    public javafx.scene.control.Label label_Deliverer;
    public javafx.scene.control.Label label_Meal;
    public javafx.scene.control.Label label_Count_Meal;
    public javafx.scene.control.Label label_Count_Client;
    public javafx.scene.control.Label label_Count_Deliverer;
    public Button CreateMealButton;
    public TextField MealNameTextField;
    public TextField PriceTextField;
    public TextField WeightTextField;
    public SplitMenuButton CategoryMenu;
    public RadioButton RadioFlour;
    public RadioButton RadioWater;
    public RadioButton RadioOil;
    public RadioButton RadioMilk;
    public RadioButton RadioEggs;
    public RadioButton RadioCheese;
    public RadioButton RadioSourCreme;
    public MenuItem PizzaMenuItem;
    public MenuItem MakaronMenuItem;
    public MenuItem DeserMenuItem;
    public SplitMenuButton SubCategoryMenu;
    public TextField TimeDurationTextField;
    public Button saveButton;
    public Button readButton;
    public Button restartButton;


    /**
     * Tutaj inicjalizuje się wszystkie zmienne.
     */
    public void initialize() {
        //   EventHandler handler = new EventHandler(){};//tworzenie uchwytu dla akcji

    }

    /**
     * Dodanie klienta.
     *
     * @param mouseEvent
     */
    public void addClient(MouseEvent mouseEvent) {
        mouseEvent.getSource();//wyciąganie elementu na którym została wykonana akcji
        if (Main.getSizeOfClientList() < 40) {
            Main.addClientToList();
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()));
            button_Del_Client.setDisable(false);
        } else {
            label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()));
        }
    }

    /**
     * Usunięcie klienta.
     *
     * @param mouseEvent
     */
    public void delClient(MouseEvent mouseEvent) {
        synchronized (this) {
            if (Main.canOrderDeleteClient()) {
                Main.setClientToNotExist();
            } else {
                button_Del_Client.setDisable(true);
            }
            if (Integer.parseInt(label_Count_Client.getText()) != 0) {
                label_Count_Client.setText(Integer.toString(Integer.parseInt(label_Count_Client.getText()) - 1));
            }
            if (Main.getSizeOfClientList() == 0) {
                label_Count_Client.setText(Integer.toString(Main.getSizeOfClientList()));
            }
        }
    }

    /**
     * Dodanie dostawcy.
     *
     * @param mouseEvent
     */
    public void addDeliverer(MouseEvent mouseEvent) {
        if (Main.getSizeOfDelivererList() < 40) {
            Main.addDelivererToList();
            label_Count_Deliverer.setText(Integer.toString(Main.getSizeOfDelivererList()));
            button_Del_Deliverer.setDisable(false);
        } else {
            label_Count_Deliverer.setText(Integer.toString(Main.getSizeOfDelivererList()));
        }
    }

    /**
     * Usunięcie dostawcy.
     *
     * @param mouseEvent
     */
    public void delDeliverer(MouseEvent mouseEvent) {
        synchronized (this) {
            if (Main.canOrderDeleteDeliverer()) {
                Main.setDelivererToNotExist();
            } else {
                button_Del_Deliverer.setDisable(true);
            }
            if (Integer.parseInt(label_Count_Deliverer.getText()) != 0) {
                label_Count_Deliverer.setText(Integer.toString(Integer.parseInt(label_Count_Deliverer.getText()) - 1));
            }
            if (Main.getSizeOfClientList() == 0) {
                label_Count_Deliverer.setText(Integer.toString(Main.getSizeOfDelivererList()));
            }
        }
    }

    /**
     * Dodanie posiłku.
     *
     * @param mouseEvent
     */
    public void addMeal(MouseEvent mouseEvent) {
        Main.addMealToMealList();
        label_Count_Meal.setText(Integer.toString(Main.getMeals_list().size()));
    }

    public void openOrderView(MouseEvent mouseEvent) {
    }

    public void clickCategoryString(ActionEvent event) {
        String text = ((MenuItem) event.getSource()).getText();
        CategoryMenu.setText(text);
        SubCategoryMenu.getItems().remove(0, SubCategoryMenu.getItems().size());
        SubCategoryMenu.setText("Nie wybrano");
        if (text.equals("Pizza")) {
            MenuItem a = new MenuItem("Margheritta");
            a.setOnAction(event1 -> SubCategoryMenu.setText(a.getText()));
            MenuItem b = new MenuItem("Salami");
            b.setOnAction(event1 -> SubCategoryMenu.setText(b.getText()));
            MenuItem c = new MenuItem("Hawaiian");
            c.setOnAction(event1 -> SubCategoryMenu.setText(c.getText()));
            SubCategoryMenu.getItems().addAll(a, b, c);
        } else if (text.equals("Makaron")) {
            MenuItem a = new MenuItem("Spaghetti");
            a.setOnAction(event1 -> SubCategoryMenu.setText(a.getText()));
            MenuItem b = new MenuItem("Lasagne");
            b.setOnAction(event1 -> SubCategoryMenu.setText(b.getText()));
            SubCategoryMenu.getItems().addAll(a, b);
        } else if (text.equals("Deser")) {
            MenuItem a = new MenuItem("Naleśniki");
            a.setOnAction(event1 -> SubCategoryMenu.setText(a.getText()));
            MenuItem b = new MenuItem("Ciasto");
            b.setOnAction(event1 -> SubCategoryMenu.setText(b.getText()));
            SubCategoryMenu.getItems().addAll(a, b);
        }
    }

    private boolean checkText(String text) {
        if (text.length() == 0 || text.equals("Błąd")) {
            return false;
        }
        text = text.trim();
        char[] tmp = text.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            if (((int) tmp[i] == 32) && i > 0 && i < tmp.length - 1) {

            } else if (!((((int) tmp[i]) >= 65 && ((int) tmp[i]) <= 90) || (((int) tmp[i]) >= 97 && ((int) tmp[i]) <= 122)
                    || tmp[i] == 'ą' || tmp[i] == 'ę' || tmp[i] == 'ó' || tmp[i] == 'ś' || tmp[i] == 'ł' || tmp[i] == 'ż'
                    || tmp[i] == 'ź' || tmp[i] == 'ć' || tmp[i] == 'ń' || tmp[i] == 'Ą' || tmp[i] == 'Ę' || tmp[i] == 'Ó'
                    || tmp[i] == 'Ś' || tmp[i] == 'Ł' || tmp[i] == 'Ż' || tmp[i] == 'Ź' || tmp[i] == 'Ć' || tmp[i] == 'Ń')) {
                return false;
            }
        }
        return true;
    }

    private boolean isPrice(String text) {
        try {
            new BigDecimal(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloat(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void clickOnCreateMealButton(ActionEvent event) {
        boolean check = true;
        String mealName = MealNameTextField.getText();
        if (!checkText(mealName)) {
            MealNameTextField.setText("Błąd");
            check = false;
        }
        String price = PriceTextField.getText();
        if (!isPrice(price)) {
            PriceTextField.setText("Błąd");
            check = false;
        }
        String weight = WeightTextField.getText();
        if (!isFloat(weight)) {
            WeightTextField.setText("Błąd");
            check = false;
        }
        String preparatioTime = TimeDurationTextField.getText();
        if (!isInteger(preparatioTime)) {
            TimeDurationTextField.setText("Błąd");
            check = false;
        }
        if (CategoryMenu.getText().equals("Nie wybrano") || SubCategoryMenu.getText().equals("Nie wybrano")) {
            check = false;
        }
        LinkedList<String> components = new LinkedList<>();
        if (RadioCheese.isSelected()) {
            components.addLast(RadioCheese.getText());
        }
        if (RadioEggs.isSelected()) {
            components.addLast(RadioEggs.getText());
        }
        if (RadioFlour.isSelected()) {
            components.addLast(RadioFlour.getText());
        }
        if (RadioMilk.isSelected()) {
            components.addLast(RadioMilk.getText());
        }
        if (RadioOil.isSelected()) {
            components.addLast(RadioOil.getText());
        }
        if (RadioSourCreme.isSelected()) {
            components.addLast(RadioSourCreme.getText());
        }
        if (RadioWater.isSelected()) {
            components.addLast(RadioWater.getText());
        }
        if (!(RadioWater.isSelected() || RadioSourCreme.isSelected() || RadioEggs.isSelected()
                || RadioCheese.isSelected() || RadioOil.isSelected() || RadioFlour.isSelected() || RadioMilk.isSelected())) {
            check = false;
        }


        if (check) {
            Main.addMealToMealList(mealName, price, weight, CategoryMenu.getText(), SubCategoryMenu.getText(), preparatioTime, components.toArray(new String[0]));
            System.out.println("Dodałem nowy posiłek.");
            RadioMilk.setSelected(false);
            RadioWater.setSelected(false);
            RadioOil.setSelected(false);
            RadioFlour.setSelected(false);
            RadioSourCreme.setSelected(false);
            RadioCheese.setSelected(false);
            RadioEggs.setSelected(false);
            MealNameTextField.setText("");
            PriceTextField.setText("");
            WeightTextField.setText("");
            TimeDurationTextField.setText("");
            CategoryMenu.setText("Nie wybrano");
            SubCategoryMenu.getItems().remove(0, SubCategoryMenu.getItems().size());
            SubCategoryMenu.setText("Nie wybrano");
        } else {
            System.out.println("Błędne wypełnienie pola.");
        }
        label_Count_Meal.setText(Integer.toString(Main.getMeals_list().size()));

    }

    public void saveAllToFile(ActionEvent event) {
        Main.saveAll();
        System.out.println("Zapisano");
    }

    public void readAllFromFile(ActionEvent event) {
        //      Main.readAll();
        System.out.println("Wczytano");
    }

    public void restartClick(ActionEvent event) {
        Main.delAllFile();

        System.out.println("Usunąłem");
    }
}
