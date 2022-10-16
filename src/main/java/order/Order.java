package order;

import java.util.List;

public class Order {

    private List<String> ingredients;
    private int orderNumber;

    public Order(int orderNumber, List<String> ingredients) {
        this.orderNumber = orderNumber;
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
