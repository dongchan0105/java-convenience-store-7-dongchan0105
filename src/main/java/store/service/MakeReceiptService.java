package store.service;

import java.util.List;
import store.domain.Receipt;

public class MakeReceiptService {

    public static int calculatePromoDiscount(List<Receipt> receipts) {
        return receipts.stream()
                .mapToInt(receipt -> receipt.getEachPrice() * receipt.getGiveaway())
                .sum();
    }

    public static int calculateMembershipDiscount(List<Receipt> receipts, boolean membership) {
        if (!membership) {
            return 0;
        }
        int nonPromotionProducts = receipts.stream()
                .filter(receipt -> receipt.getNonPromoQuantity() != 0)
                .mapToInt(receipt -> (receipt.getNonPromoQuantity()) * receipt.getEachPrice())
                .sum();
        int discount = (int) (nonPromotionProducts * 0.3);
        return Math.min(discount, 8000); // 최대 8000원 할인
    }

    public static int calculateTotalPurchase(List<Receipt> receipts) {
        return receipts.stream()
                .mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity())
                .sum();
    }

    public static int calculateFinalPrice(List<Receipt> receipts, int promoDiscount, int membershipDiscount) {
        return calculateTotalPurchase(receipts) - promoDiscount - membershipDiscount;
    }
}
