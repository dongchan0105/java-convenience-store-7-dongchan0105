package store.domain;

public class Receipt {
    private final String productName;
    private final int quantity;
    private final int basePrice;
    private final int promoDiscount;
    private final int membershipDiscount;
    private final int finalPrice;
    private final int giveaway;

    public Receipt(String productName, int quantity, int basePrice, int promoDiscount, int membershipDiscount, int finalPrice, int giveaway) {
        this.productName = productName;
        this.quantity = quantity;
        this.basePrice = basePrice;
        this.promoDiscount = promoDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalPrice = finalPrice;
        this.giveaway = giveaway;
    }

    // Getters for each field
    // Example: public String getProductName() { return productName; }
}

