package order;

import user.User;

import java.util.List;

public class UserOrders {

    private User user;
    private List<Order> orders;

    public UserOrders(User user, List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
