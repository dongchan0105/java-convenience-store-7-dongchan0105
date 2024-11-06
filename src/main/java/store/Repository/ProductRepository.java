package store.Repository;

import static store.ENUM.ErrorCode.IO_ERROR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.*;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository(){
        products=new ArrayList<>();
        loadProducts();
    }


    private void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"))) {
            reader.lines().forEach(line -> {String[] parts = line.split(",");
                Product product = getProduct(parts);
                products.add(product);}
            );
        } catch (IOException e) {
            System.out.println(IO_ERROR);
        }
    }


    private static Product getProduct(String[] parts) {
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotion = parts[3];
        Product product = new Product(name, price, quantity, promotion);
        return product;
    }



    public List<Product> getAllProducts(){
        return products;
    }

    public Product findByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElse(null);
    }
}
