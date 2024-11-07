package store.View;

public class OutputView {

    private void printReceipt(String productName, int quantity, int basePrice, int promoDiscount, int membershipDiscount, int finalPrice, int giveaway) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        System.out.printf("%s\t\t%d\t%d\n", productName, quantity, basePrice);

        System.out.println("=============증	정===============");
        if (giveaway > 0) {
            System.out.printf("%s\t\t%d\n", productName, giveaway);
        }

        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\n", basePrice);
        System.out.printf("행사할인\t\t-%d\n", promoDiscount);
        System.out.printf("멤버십할인\t\t-%d\n", membershipDiscount);
        System.out.printf("내실돈\t\t%d\n", finalPrice);
    }
}
