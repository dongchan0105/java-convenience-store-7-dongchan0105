package store.service;

import java.util.Map;
import store.Repository.ProductRepository;
import store.domain.Product;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void processPurchase(Map<String, Integer> purchaseMap) {
        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product useproduct = isEligibleForPromotion(productName,quantity);

        }
    }

    private Product isEligibleForPromotion(String productName, int quantity) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() != null)
                .filter(p -> p.getQuantity() >= quantity)
                .findFirst()
                .orElse(productRepository.findByName(productName));
    }
}

