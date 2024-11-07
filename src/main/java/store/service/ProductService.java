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
        List<Receipt> receiptsElement = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : purchaseMap.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = productRepository.findByName(productName);
            receiptsElement.add(processProductWithPromotion(product, quantity));
        }
        return receiptsElement;
    }

    private Receipt processProductWithPromotion(Product product, int quantity) {
        Product eligibleProduct = isApplyPromotion(product.getName(), quantity);
        if (eligibleProduct.getPromotion() != null) {
            return createReceipt(eligibleProduct, quantity,0);
        }
        return handlePromotion(eligibleProduct, quantity);
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
        int additionalQuantity = calculateAdditionalQuantity(product, quantity);

        if (additionalQuantity > 0) {
            requestAdditionalPurchase(product, additionalQuantity);
            if (InputController.getUserConfirm()) {
                quantity += additionalQuantity;
                giveaway = calculatePromoQuantity(quantity, product.getPromotion());
            }
        }
        return createReceipt(product, quantity, giveaway);
        // 컨트롤러로 넘겨 처리
    }

    private int calculatePromoQuantity(int quantity, String promotion) {
        String[] parts = promotion.replaceAll("[^0-9+]", "").split("\\+");
        int promoQuantity = Integer.parseInt(parts[0]) + 1;
        return quantity / promoQuantity;
    }

    private int calculateAdditionalQuantity(Product product, int quantity) {
        String[] parts = product.getPromotion().replaceAll("[^0-9+]", "").split("\\+");
        int buyQuantity = Integer.parseInt(parts[0]);
        int getQuantity = Integer.parseInt(parts[1]);
        int promoThreshold = buyQuantity + getQuantity;

        return promoThreshold - (quantity % promoThreshold);
    }

    private void requestAdditionalPurchase(Product product, int additionalQuantity) {
        InputController.showAdditionalQuantityMessage(product.getName(), additionalQuantity);
    }

    private Receipt createReceipt(Product product, int quantity, int giveaway) {
        int basePrice = calculateBasePrice(product, quantity);
        int promoDiscount = calculatePromoDiscount(product, giveaway);
        int membershipDiscount = calculateMembershipDiscount(basePrice, promoDiscount);
        int finalPrice = calculateFinalPrice(basePrice, promoDiscount, membershipDiscount);

        return new Receipt(product.getName(), quantity, basePrice, promoDiscount, membershipDiscount, finalPrice, giveaway);
    }

    private int calculateBasePrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    private int calculatePromoDiscount(Product product, int giveaway) {
        return product.getPrice() * giveaway;
    }

    private int calculateMembershipDiscount(int basePrice, int promoDiscount) {
        boolean hasMembership = InputController.isHaveMembership();
        int membershipDiscount = hasMembership ? (int) ((basePrice - promoDiscount) * 0.3) : 0;
        return Math.min(membershipDiscount, 8000); // 최대 8000원 할인
    }

    private int calculateFinalPrice(int basePrice, int promoDiscount, int membershipDiscount) {
        return basePrice - promoDiscount - membershipDiscount;
    }
}
