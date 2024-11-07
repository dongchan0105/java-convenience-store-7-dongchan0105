package store.domain;

public class Receipt {
    private final String productName;
    private final int quantity;
    private final int basePrice;
    private final int giveaway;

    public Receipt(String productName, int quantity, int basePrice, int giveaway) {
        this.productName = productName;
        this.quantity = quantity;
        this.basePrice = basePrice;
        this.giveaway = giveaway;
    }

}

