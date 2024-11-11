package store.view;

import static store.constant.Constants.ADDITIONAL_STATUS;
import static store.constant.Constants.DIVISION_LINE;
import static store.constant.Constants.EVENT_DISCOUNT;
import static store.constant.Constants.GIVEAWAY_FORM;
import static store.constant.Constants.MEMBERSHIP_DISCOUNT;
import static store.constant.Constants.ORDER_FORM;
import static store.constant.Constants.PRESENTATION_DIVISION_LINE;
import static store.constant.Constants.RECEIPT_START_FORM;
import static store.constant.Constants.TOTAL_PAYMENT;
import static store.constant.Constants.TOTAL_PURCHASE;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.controller.InputController;
import store.domain.Receipt;
import store.validation.Validation;

public class OutputView {

    public static void printReceipt(List<Receipt> receipts, boolean membership) {
        System.out.println(RECEIPT_START_FORM);

        printOrderForm(receipts);
        System.out.println(PRESENTATION_DIVISION_LINE);
        printGiveawayItems(receipts);
        System.out.println(DIVISION_LINE);

        printFinalResult(receipts, membership);
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
                .forEach(receipt -> System.out.printf(GIVEAWAY_FORM, receipt.getProductName(), receipt.getGiveaway()));
    }

    private static void printTotalPurchase(List<Receipt> receipts) {
        int totalAmount = receipts.stream().mapToInt(Receipt::getQuantity).sum();
        int totalPrice = receipts.stream().mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity()).sum();
        System.out.printf(TOTAL_PURCHASE, totalAmount, totalPrice); // 총구매액
    }

    private static void printFinalResult(List<Receipt> receipts, boolean membership) {
        int promoDiscount = calculatePromoDiscount(receipts);
        int membershipDiscount = calculateMembershipDiscount(receipts, membership);
        int finalPrice = calculateFinalPrice(receipts, promoDiscount, membershipDiscount);

        printTotalPurchase(receipts);
        System.out.printf(EVENT_DISCOUNT, String.format("-%,d", promoDiscount)); // 항상 -를 붙여 출력
        System.out.printf(MEMBERSHIP_DISCOUNT, String.format("-%,d", membershipDiscount)); // 항상 -를 붙여 출력
        System.out.printf(TOTAL_PAYMENT, finalPrice);
    }


    private static int calculatePromoDiscount(List<Receipt> receipts) {
        return receipts.stream()
                .mapToInt(receipt -> receipt.getEachPrice() * receipt.getGiveaway())
                .sum();
    }

    private static int calculateMembershipDiscount(List<Receipt> receipts, boolean membership) {
        if (!membership) {
            return 0;}
        return receipts.stream()
                .mapToInt(receipt -> {
                    int itemTotalPayPrice = (receipt.getQuantity() - receipt.getGiveaway()) * receipt.getEachPrice();
                    int discountTarget = Math.min(itemTotalPayPrice, 8000);
                    return (int) (discountTarget * 0.3);
                })
                .sum();
    }

    private static int calculateFinalPrice(List<Receipt> receipts, int promoDiscount, int membershipDiscount) {
        int totalPurchase = receipts.stream().mapToInt(receipt -> receipt.getEachPrice() * receipt.getQuantity()).sum();
        return totalPurchase - promoDiscount - membershipDiscount;
    }

    public static boolean additionalPurchaseStatus() {
        System.out.println(ADDITIONAL_STATUS);
        String input = Console.readLine();
        while (true) {
            try {
                Validation.yesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println(ADDITIONAL_STATUS);
                input = Console.readLine();
            }
        }
    }
}
