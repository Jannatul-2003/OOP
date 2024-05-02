import java.lang.management.MemoryUsage;
import java.util.*;

class FoodDelivery {

    Menu menu = new Menu();
    static ArrayList<Order> orders = new ArrayList<>();
    static int orderDelivered = 0;
    static int limit=3;
    static int perDel=5;
    static Object lock = new Object();

    class InvalidOrderException extends Exception {
        InvalidOrderException() {
            super("Invalid Order");
        }

        public String toString() {
            return "Invalid Order";
        }
    }

    class OutOfStockException extends Exception {
        OutOfStockException() {
            super(" Out of Stock");
        }

        public String toString() {
            return " Out of Stock";
        }
    }

    class ItemNotFoundException extends Exception {
        ItemNotFoundException() {
            super(" Item Not Found");
        }

        public String toString() {
            return " Item Not Found";
        }
    }

    boolean outOfStockException(FoodItem foodItem) {
        try {
            if (!foodItem.isInStock())
                throw new OutOfStockException();
        } catch (Exception e) {
            System.out.println(foodItem.getName()+e);
            return true;
        }
        return false;
    }

    boolean invalidOrderException(int quantity) {
        try {
            if (quantity < 0)
                throw new InvalidOrderException();
        } catch (Exception e) {
            System.out.println(e);
            return true;
        }
        return false;
    }

    boolean itemNotFoundException(String name) {
        try {
            if (!menu.isFoodItemAvailable(name))
                throw new ItemNotFoundException();
        } catch (Exception e) {
            System.out.println(name+e);
            return true;
        }
        return false;
    }

    class Person {
        private final String name;
        private String contactInfo;

        Person(String name, String contactInfo) {
            this.name = name;
            this.contactInfo = contactInfo;
        }

        public String getName() {
            return name;
        }

        public String getContactInfo() {
            return contactInfo;
        }
    }

    class Customer extends Person {
        //private final String address;
        Customer(String name, String contactInfo/* , String address */) {
            super(name, contactInfo);
            // this.address = address;
        }
        /*
         * public String getAddress(){
         * return address;
         * }
         */
    }

    class DeliveryPersonnel extends Person {
        private ArrayList<Order> orders;

        DeliveryPersonnel(String name, String contactInfo,ArrayList<Order> orders) {
            super(name, contactInfo);
            this.orders=orders;
        }
        public void getOrders() {
            System.out.println("Delivered by : "+getName());
            for (Order order : orders) {
                order.getOrderDetails();
            }
        }
    }

    class FoodItem {
        private final String name;
        private double price;
        private String description;
        private boolean inStock;

        public FoodItem(String name, double price, String description, boolean inStock) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.inStock = inStock;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public boolean isInStock() {
            return inStock;
        }

        public void setInStock(boolean inStock) {
            this.inStock = inStock;
        }

        public void updateDetails(String description, double price, boolean inStock) {
            this.price = price;
            this.description = description;
            this.inStock = inStock;
        }
    }

    class Menu {
        private HashMap<String, FoodItem> foodItems;

        public Menu() {
            foodItems = new HashMap<>();
        }

        public void addFoodItem(FoodItem foodItem) {
            if(outOfStockException(foodItem))
            return;
            foodItems.put(foodItem.getName(), foodItem);
        }

        public void removeFoodItem(FoodItem foodItem) {
            if (itemNotFoundException(foodItem.getName()))
                return;
            foodItems.remove(foodItem.getName());
        }

        public void updateFoodItem(FoodItem foodItem, double price) {
            removeFoodItem(foodItem);
            FoodItem newFoodItem = new FoodItem(foodItem.getName(), price, foodItem.getDescription(),
                    foodItem.isInStock());
            addFoodItem(newFoodItem);
        }

        public void updateFoodItem(FoodItem foodItem, String description) {
            removeFoodItem(foodItem);
            FoodItem newFoodItem = new FoodItem(foodItem.getName(), foodItem.getPrice(), description,
                    foodItem.isInStock());
            addFoodItem(newFoodItem);
        }

        public boolean isFoodItemAvailable(String foodItemName) {
            return foodItems.containsKey(foodItemName);
        }

        public void getFoodItems() {
            for (Map.Entry<String, FoodItem> entry : foodItems.entrySet()) {
                System.out.println("Name: " + entry.getKey());
                System.out.println("Price: " + entry.getValue().getPrice());
                System.out.println("Description: " + entry.getValue().getDescription());
                System.out.println("In Stock: " + entry.getValue().isInStock());
                System.out.println();
            }
        }

    }

    enum OrderStatus {
        PENDING, CONFIRMED, UPDATED, CANCELED, DELIVERED
    }

    class Order {
        private final Customer customer;
        private final HashMap<FoodItem, Integer> foodItems;
        private double totalPrice;
        protected OrderStatus status;

        public Order(Customer customer) {
            this.customer = customer;
            foodItems = new HashMap<>();
            status = OrderStatus.PENDING;
            totalPrice = 0;
        }

        boolean cannotUpdate(OrderStatus status) {
            if (status == OrderStatus.CONFIRMED) {
                System.out.println("Order cannot be changed or canceled at this stage");
                return true;
            }
            return false;
        }

        public void addFoodItem(String food_name, int quantity) {
            if (cannotUpdate(status))
                return;
            if (invalidOrderException(quantity))
                return;
            if (itemNotFoundException(food_name))
                return;
            FoodItem foodItem = menu.foodItems.get(food_name);
            FoodItem OrderedFoodItem = new FoodItem(food_name, foodItem.getPrice(), foodItem.getDescription(),
                    foodItem.isInStock());
            foodItems.put(OrderedFoodItem, quantity);
            totalPrice += OrderedFoodItem.getPrice() * quantity;
        }

        public void removeFoodItem(String food_name) {
            if (cannotUpdate(status))
                return;
            if (itemNotFoundException(food_name))
                return;
            boolean wasOrdered = false;
            for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
                if (entry.getKey().getName().equals(food_name)) {
                    totalPrice -= entry.getKey().getPrice() * entry.getValue();
                    foodItems.remove(entry.getKey());
                    status = OrderStatus.UPDATED;
                    return;
                }
            }
            if (!wasOrdered)
                System.out.println("Food Item was not ordered");
        }

        public void updateFoodItem(String name, int quantity) {
            if (cannotUpdate(status))
                return;
            if (invalidOrderException(quantity))
                return;
            if (itemNotFoundException(name))
                return;
            for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
                if (entry.getKey().getName().equals(name)) {
                    totalPrice -= entry.getKey().getPrice() * entry.getValue();
                    entry.setValue(quantity);
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
                    status = OrderStatus.UPDATED;
                    return;
                }
            }
            addFoodItem(name, quantity);
        }

        public void cancelOrder() {
            if (cannotUpdate(status))
                return;
            status = OrderStatus.CANCELED;
            if (orders.contains(this))
                orders.remove(this);
        }

        public void getOrderDetails() {
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("Total Price: " + totalPrice);
            System.out.println("Order Status: " + status);
            System.out.println("Food Items: \n");
            for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
                System.out.println("Name: " + entry.getKey().getName()+" Price: " + entry.getKey().getPrice()+" Quantity: " + entry.getValue());
            }
        }
    }

    class OrderThread extends Thread {
        OrderThread() {
            super("OrderThread");
        }

        public void run() {
            for (int i = 0; i < limit; i++) {
                try {
                    Customer Rahul = new Customer("Rahul" + i, "9876543210" + i);
                    Order order1 = new Order(Rahul);
                    order1.addFoodItem("Pizza", 2 + i);
                    order1.addFoodItem("Burger", 3 + i - 1);
                    order1.addFoodItem("Pasta", 3-i);
                    order1.addFoodItem("Noodles", 2);
                    order1.updateFoodItem("Burger", 2);
                    order1.removeFoodItem("Pasta");
                    order1.getOrderDetails();
                    orders.add(order1);
                    // here we can cancel the order
                    order1.status = OrderStatus.CONFIRMED;// no more change
                    order1.cancelOrder();
                    Thread.sleep(800);
                    if (orders.size() ==1) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }
    }
    class DeliveryThread extends Thread{
        DeliveryThread()
        {
            super("DeliveryThread");
        }
        public void run()
        {
            while(orderDelivered<limit)
            {
                try{
                    synchronized(lock)
                    {
                        while(orders.size()==0)
                        lock.wait();
                    }
                    int numElementsToExtract = Math.min(perDel,orders.size());
                    ArrayList<Order> assignOrder=new ArrayList<>();
                    for(int i=0; i<numElementsToExtract; i++)
                    assignOrder.add(orders.get(i));
                    for(int i=0; i<numElementsToExtract; i++)
                    orders.remove(0);

                    DeliveryPersonnel bijoy=new DeliveryPersonnel("Bijoy"+orderDelivered, "0171621366"+orderDelivered,assignOrder);
                    for(Order order: assignOrder)
                    {
                        order.status=OrderStatus.DELIVERED;
                        orderDelivered++;
                    }
                    bijoy.getOrders();
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
    }

    void execute() {
        FoodItem Pizza = new FoodItem("Pizza", 200, "Cheese Burst", true);
        FoodItem Burger = new FoodItem("Burger", 100, "Veg Burger", true);
        FoodItem Pasta = new FoodItem("Pasta", 150, "White Sauce Pasta", true);
        FoodItem Noodles = new FoodItem("Noodles", 120, "Hakka Noodles", true);
        FoodItem IceCream = new FoodItem("Ice Cream", 50, "Vanilla", true);
        FoodItem Cake = new FoodItem("Cake", 250, "Chocolate Cake", true);
        FoodItem Juice = new FoodItem("Juice", 30, "Orange Juice", true);
        FoodItem Coffee = new FoodItem("Coffee", 40, "Cold Coffee", false);

        menu.addFoodItem(Pizza);
        menu.addFoodItem(Burger);
        menu.addFoodItem(Pasta);
        menu.addFoodItem(Noodles);
        menu.addFoodItem(IceCream);
        menu.addFoodItem(Cake);
        menu.addFoodItem(Juice);
        menu.addFoodItem(Coffee);
        menu.updateFoodItem(Coffee, "Hot Coffee");
        menu.updateFoodItem(Coffee, 50);
        System.out.println("---------------Menu Started---------------");
        menu.getFoodItems();
        System.out.println("---------------Menu Ended---------------");
       OrderThread a=new OrderThread();
        DeliveryThread b=new DeliveryThread();
        a.start();
        b.start();
    }
public static void main(String[] args) {
    FoodDelivery s=new FoodDelivery();
    s.execute();
}
}
