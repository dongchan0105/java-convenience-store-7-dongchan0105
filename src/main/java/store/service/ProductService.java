package store.service;

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
            calculateFinalPrice(eligibleProduct, quantity, 0);
            return;
        }
        handlePromotion(eligibleProduct, quantity);
    }

    private void handlePromotion(Product product, int quantity) {
        int giveaway = calculatePromoQuantity(quantity, product.getPromotion());
        String[] parts = product.getPromotion().replaceAll("[^0-9+]", "").split("\\+");
        int buyQuantity = Integer.parseInt(parts[0]); // 프로모션 조건에서 필요한 구매 수량 예: 2
        int getQuantity = Integer.parseInt(parts[1]); // 증정 수량 예: 1
        int promoThreshold = buyQuantity + getQuantity; // 예를 들어, 2+1 프로모션의 경우 3

        // 추가로 필요한 수량 계산
        int remainingForNextPromo = promoThreshold - (quantity % promoThreshold);

        if (remainingForNextPromo > 0 && remainingForNextPromo < buyQuantity) {
            inputView.showAdditionalQuantityMessage(product.getName(), remainingForNextPromo);

            // 사용자가 추가 구매를 원할 경우
            if (inputView.getUserConfirmation()) {
                quantity += remainingForNextPromo; // 추가 수량을 포함한 최종 수량
                giveaway= calculatePromoQuantity(quantity, product.getPromotion());
            }
        }

        // 최종 계산 및 결제 로직
        calculateFinalPrice(product, quantity, giveaway);
    }


    private int calculatePromoQuantity(int quantity, String promotion) {
        String[] parts = promotion.replaceAll("[^0-9+]", "").split("\\+");
        int promoQuantity = Integer.parseInt(parts[0])+1;
        return  (quantity / promoQuantity);
    }

    private Receipt calculateFinalPrice(Product product, int quantity, int giveaway) {
        // 1. 총 구매 금액 계산
        int basePrice = product.getPrice() * quantity;

        // 2. 행사 할인 금액 계산
        int promoDiscount = product.getPrice() * giveaway;

        // 3. 멤버십 할인 적용
        boolean hasMembership = InputController.isHaveMembership();
        int membershipDiscount = hasMembership ? (int) ((basePrice - promoDiscount) * 0.3) : 0;
        membershipDiscount = Math.min(membershipDiscount, 8000); // 최대 8000원 할인

        // 4. 최종 결제 금액 계산
        int finalPrice = basePrice - promoDiscount - membershipDiscount;

        // 5. 영수증 출력
        return new Receipt(product.getName(), quantity, basePrice, promoDiscount, membershipDiscount, finalPrice, giveaway);
    }

}


