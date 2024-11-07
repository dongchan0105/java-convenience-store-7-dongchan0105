package store.service;

import java.util.Map;
import store.Repository.ProductRepository;
import store.View.InputView;
import store.domain.Product;

public class ProductService {
    private final ProductRepository productRepository;
    private final InputView inputView;

    public ProductService(ProductRepository productRepository, InputView inputView) {
        this.productRepository = productRepository;
        this.inputView = inputView;
    }

    public void processPurchase(Map<String, Integer> purchaseMap) {
        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = productRepository.findByName(productName);
            processProductWithPromotion(product, quantity);
        }
    }

    private Product isApplyPromotion(String productName, int quantity) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() != null)
                .filter(p -> p.getQuantity() >= quantity)
                .findFirst()
                .orElse(productRepository.findByName(productName));
    }

    private void processProductWithPromotion(Product product, int quantity) {
        Product eligibleProduct = isApplyPromotion(product.getName(), quantity);
        if (eligibleProduct.getPromotion() != null) {
            calculateFinalPrice(eligibleProduct, quantity, false);
            return;
        }
        handlePromotion(eligibleProduct, quantity);
    }

    private void handlePromotion(Product product, int quantity) {
        int promoQuantity = calculatePromoQuantity(quantity, product.getPromotion());
        int additionalQuantity = promoQuantity - quantity;

        if (additionalQuantity > 0) { // 추가 증정 가능
            inputView.showAdditionalQuantityMessage(product.getName(), additionalQuantity);
            if (inputView.getUserConfirmation()) {
                quantity += additionalQuantity; // 추가 수량 포함
            }
        }

        calculateFinalPrice(product, quantity, false);
    }

    private int calculatePromoQuantity(int quantity, String promotion) {
        String[] parts = promotion.replaceAll("[^0-9+]", "").split("\\+");
        int promoQuantity = Integer.parseInt(parts[0]);
        return  quantity / promoQuantity;
    }

    private void calculateFinalPrice(Product product, int quantity, boolean applyFullPrice) {

    }


}


