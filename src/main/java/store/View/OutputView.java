package store.View;

import static store.Constant.Constants.ADDITIONAL_STATUS;
import static store.Constant.Constants.DIVISION_LINE;
import static store.Constant.Constants.EVENT_DISCOUNT;
import static store.Constant.Constants.MEMBERSHIP_DISCOUNT;
import static store.Constant.Constants.ORDER_FORM;
import static store.Constant.Constants.PRESENTATION_DIVISION_LINE;
import static store.Constant.Constants.RECEIPT_START_FORM;
import static store.Constant.Constants.TOTAL_PAYMENT;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.domain.Receipt;

public class OutputView {

    public static void printReceipt(List<Receipt> receipts, boolean membership) {
        System.out.println(RECEIPT_START_FORM);

        printOrderForm(receipts);
        System.out.println(PRESENTATION_DIVISION_LINE);
        printGiveawayItems(receipts);
        System.out.println(DIVISION_LINE);

        printFinalResult(receipts, membership);
    }

    private static void printFinalResult(List<Receipt> receipts, boolean membership) {
        int promoDiscount = calculatePromoDiscount(receipts);
        int membershipDiscount = calculateMembershipDiscount(receipts, membership);
        int finalPrice = calculateFinalPrice(receipts, promoDiscount, membershipDiscount);

        printTotalPurchase(receipts);
        System.out.printf(EVENT_DISCOUNT, promoDiscount);
        System.out.printf(MEMBERSHIP_DISCOUNT, membershipDiscount);
        System.out.printf(TOTAL_PAYMENT, finalPrice);
    }

    private static void printOrderForm(List<Receipt> receipts) {
        receipts.forEach(receipt ->
                System.out.printf(ORDER_FORM, receipt.getProductName(), receipt.getQuantity(),
                        receipt.getEachPrice() * receipt.getQuantity())
        );
    }

    private static void printGiveawayItems(List<Receipt> receipts) {
        receipts.stream()
                .filter(receipt -> receipt.getGiveaway() > 0)
                .forEach(receipt -> System.out.printf("%s\t\t%d\n", receipt.getProductName(), receipt.getGiveaway()));
    }

    private static void printTotalPurchase(List<Receipt> receipts) {
        int totalAmount = receipts.stream().mapToInt(Receipt::getQuantity).sum();
        int totalPrice = receipts.stream().mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity()).sum();
        System.out.printf("총구매액\t\t%d\t%d\n", totalAmount, totalPrice);
    }

    private static int calculatePromoDiscount(List<Receipt> receipts) {
        return receipts.stream()
                .mapToInt(receipt -> receipt.getEachPrice() * receipt.getGiveaway())
                .sum();
    }

    private static int calculateMembershipDiscount(List<Receipt> receipts, boolean membership) {
        if (!membership) {
            return 0;
        }
        int totalPurchase = receipts.stream().mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity()).sum();
        int discount = (int) (totalPurchase * 0.3);
        return Math.min(discount, 8000); // 최대 8000원 할인
    }

    private static int calculateFinalPrice(List<Receipt> receipts, int promoDiscount, int membershipDiscount) {
        int totalPurchase = receipts.stream().mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity()).sum();
        return totalPurchase - promoDiscount - membershipDiscount;
    }

    public static boolean additionalPurchaseStatus() {
        System.out.println(ADDITIONAL_STATUS);
        return Console.readLine().equalsIgnoreCase("Y");
    }

}

