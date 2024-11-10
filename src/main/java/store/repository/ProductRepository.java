package store.repository;

import static store.ENUM.ErrorCode.IO_ERROR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.*;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        products = new ArrayList<>();
        loadProducts();
        loadPromotions();
    }


    private void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"))) {
            reader.readLine();
            reader.lines().forEach(this::addProductList
            );
        } catch (IOException e) {
            System.out.println(IO_ERROR);
        }
    }

    private void addProductList(String line) {
        String[] parts = line.split(",");
        Product product = getProduct(parts);
        products.add(product);
    }

    private void loadPromotions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md"))) {
            reader.readLine();
            reader.lines().forEach(ProductRepository::getPromotions);
        } catch (IOException e) {
            System.out.println("[ERROR] 프로모션 파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    private static void getPromotions(String line) {
        PromotionComponent promoComponent = getPromotionComponent(line);

        Promotion.addPromotion(promoComponent.name(), promoComponent.buyQuantity(), promoComponent.getQuantity(), promoComponent.startDate(),
                promoComponent.endDate());
    }

    private static PromotionComponent getPromotionComponent(String line) {
        String[] parts = line.split(",");
        String name = parts[0].trim();
        int buyQuantity = Integer.parseInt(parts[1].trim());
        int getQuantity = Integer.parseInt(parts[2].trim());
        String startDate = parts[3].trim();
        String endDate = parts[4].trim();
        return new PromotionComponent(name, buyQuantity, getQuantity, startDate, endDate);
    }

    private record PromotionComponent(String name, int buyQuantity, int getQuantity, String startDate, String endDate) {
    }


    private static Product getProduct(String[] parts) {
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotion = parts[3];
        return new Product(name, price, quantity, promotion);
    }


    public List<Product> getAllProducts() {
        return products;
    }

    public Product findAnyByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElse(null);
    }

    public Product findByNameWithNonPromo(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .filter(product -> product.getPromotion() == null)
                .findFirst()
                .orElse(null);
    }

    public Product findByNameWithPromo(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .filter(product -> product.getPromotion() != null)
                .findFirst()
                .orElse(null);
    }

    public void reflectPurchase(Receipt receipt) {
        int remainingQuantity = receipt.getQuantity();
        remainingQuantity = subtractPromoQuantity(receipt.getProductName(), remainingQuantity);
        subtractNonPromoQuantity(receipt.getProductName(), remainingQuantity);
    }

    private int subtractPromoQuantity(String productName, int remainingQuantity) {
        Product promoProduct = findByNameWithPromo(productName);
        if (promoProduct != null && promoProduct.getQuantity() > 0) {
            int promoQuantity = Math.min(promoProduct.getQuantity(), remainingQuantity);
            promoProduct.subtraction(promoQuantity);
            remainingQuantity -= promoQuantity;
        }
        return remainingQuantity;
    }

    private void subtractNonPromoQuantity(String productName, int remainingQuantity) {
        if (remainingQuantity > 0) {
            Product nonPromoProduct = findByNameWithNonPromo(productName);
            if (nonPromoProduct != null) {
                nonPromoProduct.subtraction(remainingQuantity);
            }
        }
    }


}
