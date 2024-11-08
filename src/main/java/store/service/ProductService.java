package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.Controller.InputController;
import store.Repository.ProductRepository;
import store.domain.Product;
import store.domain.Receipt;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Receipt> processPurchase(Map<Product, Integer> purchaseMap) {
        List<Receipt> receipts = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : purchaseMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            receipts.add(performPayment(product, quantity));
        }
        return receipts;
    }

    private Receipt performPayment(Product product, int quantity) {
        // 프로모션 적용 가능 여부 확인
        Product eligibleProduct = isApplyPromotion(product.getName(), quantity);
        if (eligibleProduct.getPromotion() != null) {
            return handlePromotion(eligibleProduct, quantity);
        }

        // 프로모션이 없는 경우 기본 결제
        return createReceipt(product, quantity, 0);
    }

    private Product isApplyPromotion(String productName, int quantity) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() != null)
                .findFirst()
                .orElse(productRepository.findByName(productName));
    }

    private Receipt handlePromotion(Product product, int quantity) {
        // 프로모션 조건을 기반으로 최대 적용 가능 수량 및 부족한 수량 계산
        String[] parts = product.getPromotion().replaceAll("[^0-9+]", "").split("\\+");
        int buyQuantity = Integer.parseInt(parts[0]);
        int promoThreshold = buyQuantity + 1;

        int maxPromoQuantity = promoThreshold * (product.getQuantity() / promoThreshold);
        int applicablePromoQuantity = Math.min(quantity, maxPromoQuantity);
        int shortage = quantity - applicablePromoQuantity;

        // 부족한 수량에 대해 사용자에게 정가 결제 여부 확인
        if (shortage > 0) {
            InputController.showShortageMessage(product.getName(), shortage);
            if (InputController.getUserConfirm()) { // Y: 정가로 결제
                return createReceipt(product, quantity, product.getQuantity() / promoThreshold);
            }
            // N: 프로모션 수량만 결제
            return createReceipt(product, maxPromoQuantity, product.getQuantity() / promoThreshold);
        }
        int giveaway = calculatePromoQuantity(quantity, product.getPromotion());
        // 프로모션 전량 적용 가능 시
        if ((double) product.getQuantity() / promoThreshold == (double) (int) product.getQuantity() / promoThreshold) {
            return createReceipt(product, quantity, giveaway);
        }

        // 추가 구매 가능 여부를 사용자에게 확인
        int additionalPurchase = promoThreshold - (quantity % promoThreshold);
        if (InputController.getAdditionalUserConfirm(product.getName(), additionalPurchase)) {
            quantity += additionalPurchase;
            giveaway = calculatePromoQuantity(quantity, product.getPromotion());
        }

        return createReceipt(product, quantity, giveaway);
    }


    private int calculatePromoQuantity(int quantity, String promotion) {
        String[] parts = promotion.replaceAll("[^0-9+]", "").split("\\+");
        int promoQuantity = Integer.parseInt(parts[0]) + 1;
        return quantity / promoQuantity;
    }


    private Receipt createReceipt(Product product, int quantity, int giveaway) {
        return new Receipt(product.getName(), quantity, product.getPrice(), giveaway);
    }
}

