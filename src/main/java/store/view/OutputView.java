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
import store.domain.Receipt;
import store.service.MakeReceiptService;
import store.validation.Validation;

public class OutputView {

    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static void printErrorMessage(String message) {
        System.out.println(message);
    }

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
        int totalPrice = MakeReceiptService.calculateTotalPurchase(receipts);
        System.out.printf(TOTAL_PURCHASE, totalAmount, totalPrice);
    }

    private static void printFinalResult(List<Receipt> receipts, boolean membership) {
        int promoDiscount = MakeReceiptService.calculatePromoDiscount(receipts);
        int membershipDiscount = MakeReceiptService.calculateMembershipDiscount(receipts, membership);
        int finalPrice = MakeReceiptService.calculateFinalPrice(receipts, promoDiscount, membershipDiscount);

        printTotalPurchase(receipts);
        System.out.printf(EVENT_DISCOUNT, String.format("-%,d", promoDiscount));
        System.out.printf(MEMBERSHIP_DISCOUNT, String.format("-%,d", membershipDiscount));
        System.out.printf(TOTAL_PAYMENT, finalPrice);
    }

    public static boolean additionalPurchaseStatus() {
        System.out.println(ADDITIONAL_STATUS);
        String input = Console.readLine();
        try {
            Validation.yesOrNo(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return additionalPurchaseStatus();
        }
        return input.equalsIgnoreCase("Y");
    }
}
