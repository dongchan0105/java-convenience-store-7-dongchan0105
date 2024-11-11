package store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.controller.InputController;
import store.repository.ProductRepository;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.repository.PromotionRepository;

public class ProductService {
    private final ProductRepository productRepository;
    private final InputController inputController;

    public ProductService(ProductRepository productRepository, InputController inputController) {
        this.productRepository = productRepository;
        this.inputController = inputController;
    }

    public List<Receipt> getReceiptInfo(Map<Product, Integer> purchaseMap) {
        List<Receipt> receipts = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : purchaseMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            receipts.add(operationAccordingPromotion(product, quantity));
        }
        return receipts;
    }

    private Receipt operationAccordingPromotion(Product product, int quantity) {
        // 프로모션 적용 가능 여부 확인
        Product eligibleProduct = isApplyPromotion(product.getName(), quantity);
        if (eligibleProduct.getPromotion() != null) {
            return handlePromotion(eligibleProduct, quantity);
        }
        // 프로모션이 없는 경우 기본 결제
        return createReceipt(product, quantity,quantity, 0);
    }

    private Product isApplyPromotion(String productName, int quantity) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getName().equals(productName) && p.getPromotion() != null && p.getQuantity() > 0)
                .findFirst()
                .or(() -> productRepository.getAllProducts().stream()
                        .filter(p -> p.getName().equals(productName) && p.getPromotion() == null)
                        .findFirst())
                .orElse(productRepository.findAnyByName(productName));
    }


    private Receipt handlePromotion(Product product, int quantity) {
        Promotion promotion = PromotionRepository.getPromotion(product.getPromotion());
        if (!isPromotionActive(promotion)) {
            return createReceipt(product, quantity,quantity, 0);}
        int promoThreshold = calculatePromoThreshold(promotion);
        int maxPromoQuantity = calculateMaxPromoQuantity(product, promoThreshold);
        int applicablePromoQuantity = Math.min(quantity, maxPromoQuantity);
        int shortage = calculateShortage(quantity, applicablePromoQuantity);
        if (hasShortage(shortage)) {
            return handleShortage(product, quantity, promoThreshold, maxPromoQuantity, shortage);
        }
        return processAdditionalPurchase(product, quantity, promoThreshold, promotion);
    }

    private boolean isPromotionActive(Promotion promotion) {
        return promotion != null && promotion.isActive();
    }

    private int calculatePromoThreshold(Promotion promotion) {
        return promotion.getBuyQuantity() + promotion.getGiveAwayQuantity();
    }

    private int calculateMaxPromoQuantity(Product product, int promoThreshold) {
        return promoThreshold * (product.getQuantity() / promoThreshold);
    }

    private int calculateShortage(int quantity, int applicablePromoQuantity) {
        return quantity - applicablePromoQuantity;
    }

    private boolean hasShortage(int shortage) {
        return shortage > 0;
    }

    private Receipt handleShortage(Product product, int quantity, int promoThreshold, int maxPromoQuantity,
                                   int shortage) {
        InputController.showShortageMessage(product.getName(), shortage);
        if (inputController.getUserConfirm()) {
            int giveaway = product.getQuantity() / promoThreshold;
            return createReceipt(product, quantity, shortage,giveaway);
        }
        int giveaway = product.getQuantity() / promoThreshold;
        return createReceipt(product, maxPromoQuantity,0, giveaway);
    }

    private Receipt processAdditionalPurchase(Product product, int quantity, int promoThreshold, Promotion promotion) {
        int giveaway = calculatePromoQuantity(quantity, promotion);
        if (isExactPromoQuantity(quantity, promoThreshold)) {
            return createReceipt(product, quantity,0, giveaway);
        }
        if (quantity % promoThreshold == promotion.getBuyQuantity()) {
            if (inputController.getAdditionalUserConfirm(product.getName(), promotion.getGiveAwayQuantity())) {
                quantity += promotion.getGiveAwayQuantity();
                giveaway = calculatePromoQuantity(quantity, promotion);
            }
        }
        return createReceipt(product, quantity,quantity-(giveaway*promoThreshold), giveaway);
    }

    private boolean isExactPromoQuantity(int quantity, int promoThreshold) {
        return quantity % promoThreshold == 0;
    }


    private int calculatePromoQuantity(int quantity, Promotion promotion) {
        int promoQuantity = promotion.getBuyQuantity() + promotion.getGiveAwayQuantity();
        return quantity / promoQuantity;
    }


    private Receipt createReceipt(Product product, int quantity,int nonPromoQuantity, int giveaway) {
        return new Receipt(product.getName(), quantity,nonPromoQuantity, product.getPrice(), giveaway);
    }
}

