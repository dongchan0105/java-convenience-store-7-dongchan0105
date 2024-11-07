package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.Controller.InputController;
import store.Repository.ProductRepository;
import store.View.InputView;
import store.domain.Product;
import store.domain.Receipt;

public class ProductService {
    private final ProductRepository productRepository;
    private final InputView inputView;

    public ProductService(ProductRepository productRepository, InputView inputView) {
        this.productRepository = productRepository;
        this.inputView = inputView;
    }

    public List<Receipt> processPurchase(Map<String, Integer> purchaseMap) {
        List<Receipt> receipts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = productRepository.findByName(productName);
            receipts.add(processProductWithPromotion(product, quantity));
        }
        return receipts;
    }

    private Receipt processProductWithPromotion(Product product, int quantity) {
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
                .filter(p -> p.getQuantity() >= quantity)
                .findFirst()
                .orElse(productRepository.findByName(productName));
    }

    private Receipt handlePromotion(Product product, int quantity) {
        int giveaway = calculatePromoQuantity(quantity, product.getPromotion());

        // 프로모션 조건을 기반으로 최대 적용 가능 수량 및 부족한 수량 계산
        String[] parts = product.getPromotion().replaceAll("[^0-9+]", "").split("\\+");
        int buyQuantity = Integer.parseInt(parts[0]);
        int freeQuantity = Integer.parseInt(parts[1]);
        int promoThreshold = buyQuantity + freeQuantity;

        int maxPromoQuantity = promoThreshold * (product.getQuantity() / promoThreshold);
        int applicablePromoQuantity = Math.min(quantity, maxPromoQuantity);
        int shortage = quantity - applicablePromoQuantity;

        // 부족한 수량에 대해 사용자에게 정가 결제 여부 확인
        if (shortage > 0) {
            InputController.showShortageMessage(product.getName(), shortage);
            if (InputController.getUserConfirm()) { // Y: 정가로 결제
                return createReceiptWithShortage(product, applicablePromoQuantity, shortage, freeQuantity);
            }

            // N: 프로모션 수량만 결제
            return createReceipt(product, applicablePromoQuantity, giveaway);
        }

        // 프로모션 전량 적용 가능 시
        return createReceipt(product, quantity, giveaway);
    }

    private Receipt createReceiptWithShortage(Product product, int promoQuantity, int shortage, int giveaway) {
        int promoBasePrice = calculateBasePrice(product, promoQuantity);
        int shortageBasePrice = calculateBasePrice(product, shortage);

        // 최종 할인 및 결제 금액 계산
        int promoDiscount = calculatePromoDiscount(product, giveaway);
        int totalBasePrice = promoBasePrice + shortageBasePrice;

        return new Receipt(product.getName(), promoQuantity + shortage, totalBasePrice, giveaway);
    }

    private int calculatePromoQuantity(int quantity, String promotion) {
        String[] parts = promotion.replaceAll("[^0-9+]", "").split("\\+");
        int promoQuantity = Integer.parseInt(parts[0]) + 1;
        return quantity / promoQuantity;
    }

    private int calculateBasePrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    private int calculatePromoDiscount(Product product, int giveaway) {
        return product.getPrice() * giveaway;
    }

    private Receipt createReceipt(Product product, int quantity, int giveaway) {
        int basePrice = calculateBasePrice(product, quantity);
        int promoDiscount = calculatePromoDiscount(product, giveaway);

        return new Receipt(product.getName(), quantity, basePrice, giveaway);
    }
}

