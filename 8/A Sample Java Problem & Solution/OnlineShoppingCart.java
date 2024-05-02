import java.util.*;

class ShoppingCart {
    private class Item {
        String name;
        int quantity;
        double price;

        Item(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
        void updateQuantity(int quantity) {
            this.quantity = quantity;
            if(this.quantity <= 0) {
                cart.remove(this.name);
            }
        }
    }

    private Map<String, Item> cart = new HashMap<>();

    public void addItem(String name, int quantity, double price) {
        if (cart.containsKey(name)) {
            cart.get(name).updateQuantity(quantity);
            System.out.println("Quantity of "+name +" updated to "+quantity);
        } else {
            cart.put(name, new Item(name, quantity, price));
            System.out.println(name+" added");
        }
    }

    public void removeItem(String name) {
        if (cart.remove(name) != null) {
            System.out.println(name+" removed");
        } else {
            System.out.println("Error: "+name+" not found");
        }
    }

    public void updateQuantity(String name, int quantity) {
        if (cart.containsKey(name)) {
            cart.get(name).updateQuantity(quantity);
            System.out.println(name+" updated");
        } else {
            System.out.println("Error: "+name+" not found");
        }
    }

    
    private void heapify(Item[] items, int n, int i) {
        int largest = i;  
        int left = 2 * i + 1;  
        int right = 2 * i + 2;  

        if (left < n && items[left].name.compareTo(items[largest].name) > 0) {
            largest = left;
        }

        if (right < n && items[right].name.compareTo(items[largest].name) > 0) {
            largest = right;
        }

        if (largest != i) {
            Item temp = items[i];
            items[i] = items[largest];
            items[largest] = temp;

            heapify(items, n, largest);
        }
    }

    private void heapSort(Item[] items, int n) {
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(items, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            Item temp = items[0];
            items[0] = items[i];
            items[i] = temp;
            heapify(items, i, 0);
        }
    }

    public String toProperCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public void viewCart() {
        Item[] items = cart.values().toArray(new Item[0]);
        int n = items.length;
        heapSort(items, n);
        double totalCost = 0.0;
        for (Item item : items) {
            System.out.printf("%s ---- Quantity: %d x Price: BDT. %.2f%n", item.name, item.quantity, item.price);
            totalCost += item.quantity * item.price;
        }
        System.out.printf("Total cost: BDT. %.2f%n", totalCost);
    }

    public void checkout() {
        double totalCost = 0.0;
        for (Item item : cart.values()) {
            totalCost += item.quantity * item.price;
        }
        System.out.printf("Total cost: BDT. %.2f%n", totalCost);
        System.out.println("Checkout complete");
        cart.clear();
    }
}

public class OnlineShoppingCart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        boolean running = true;
        while (running) {
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ");

            if (parts.length == 0) {
                continue;
            }

            String command = parts[0].toUpperCase();
            switch (command) {
                case "ADD":
                    if (parts.length != 4) {
                        System.out.println("Invalid ADD command");
                        break;
                    }
                    String name = parts[1];
                    name=cart.toProperCase(name);
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    cart.addItem(name, quantity, price);
                    break;
                case "REMOVE":
                    if (parts.length != 2) {
                        System.out.println("Invalid REMOVE command");
                        break;
                    }
                    cart.removeItem(cart.toProperCase(parts[1]));
                    break;
                case "UPDATE":
                    if (parts.length != 3) {
                        System.out.println("Invalid UPDATE command");
                        break;
                    }
                    name = cart.toProperCase(parts[1]);
                    quantity = Integer.parseInt(parts[2]);
                    cart.updateQuantity(name, quantity);
                    break;
                case "VIEW":
                    cart.viewCart();
                    break;
                case "CHECKOUT":
                    cart.checkout();
                    scanner.close();
                    running = false;
                    break;
                default:
                    System.out.println("Unknown operation");
            }
        }
    }
}
