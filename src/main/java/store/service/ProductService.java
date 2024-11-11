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
            receipts.add(createReceiptForProduct(product, quantity));
        }
        return receipts;
    }

    private Receipt createReceiptForProduct(Product product, int quantity) {
        Promotion promotion = getEligiblePromotion(product);
        if (promotion != null && promotion.isActive()) {
            return handlePromotion(product, quantity, promotion);
        }
        return createStandardReceipt(product, quantity);
    }

    private Promotion getEligiblePromotion(Product product) {
        if (product.getPromotion() != null) {
            return PromotionRepository.getPromotion(product.getPromotion());
        }
        return null;
    }

    private Receipt handlePromotion(Product product, int quantity, Promotion promotion) {
        int promoThreshold = promotion.getBuyQuantity() + promotion.getGiveAwayQuantity();
        int maxPromoQuantity = calculateMaxPromoQuantity(product, promoThreshold);
        int applicablePromoQuantity = Math.min(quantity, maxPromoQuantity);
        int shortage = quantity - applicablePromoQuantity;

        if (shortage > 0) {
            return processShortage(product, quantity, promoThreshold, maxPromoQuantity, shortage, promotion);
        }
        return processAdditionalPurchase(product, quantity, promoThreshold, promotion);
    }

    private int calculateMaxPromoQuantity(Product product, int promoThreshold) {
        return promoThreshold * (product.getQuantity() / promoThreshold);
    }

    private Receipt processShortage(Product product, int quantity, int promoThreshold, int maxPromoQuantity, int shortage, Promotion promotion) {
        InputController.showShortageMessage(product.getName(), shortage);
        if (inputController.getUserConfirm()) {
            int giveaway = product.getQuantity() / promoThreshold;
            return createReceipt(product, quantity, shortage, giveaway);
        }
        int giveaway = product.getQuantity() / promoThreshold;
        return createReceipt(product, maxPromoQuantity, 0, giveaway);
    }

    private Receipt processAdditionalPurchase(Product product, int quantity, int promoThreshold, Promotion promotion) {
        int giveaway = calculatePromoQuantity(quantity, promotion);
        if (quantity % promoThreshold == promotion.getBuyQuantity()) {
            if (inputController.getAdditionalUserConfirm(product.getName(), promotion.getGiveAwayQuantity())) {
                quantity += promotion.getGiveAwayQuantity();
                giveaway = calculatePromoQuantity(quantity, promotion);
            }
        }
        return createReceipt(product, quantity, quantity - (giveaway * promoThreshold), giveaway);
    }

    private int calculatePromoQuantity(int quantity, Promotion promotion) {
        return quantity / (promotion.getBuyQuantity() + promotion.getGiveAwayQuantity());
    }

    private Receipt createStandardReceipt(Product product, int quantity) {
        return createReceipt(product, quantity, quantity, 0);
    }

    private Receipt createReceipt(Product product, int quantity, int nonPromoQuantity, int giveaway) {
        return new Receipt(product.getName(), quantity, nonPromoQuantity, product.getPrice(), giveaway);
    }
}
