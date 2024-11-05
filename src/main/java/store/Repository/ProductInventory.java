package store.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.ENUM.Product;

public class ProductInventory {
    private List<Product> products;

    public ProductInventory() {
        products = new ArrayList<Product>(Arrays.asList(Product.values()));
    }

    public void displayProducts(){
        for(Product p : products){
            System.out.println(p);
        }
    }


}
