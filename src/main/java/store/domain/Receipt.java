package store.domain;

public class Receipt {
    private final String productName;
    private final int quantity;
    private final int nonPromoQuantity;
    private final int eachPrice;
    private final int giveaway;

    public Receipt(String productName, int quantity,int nonPromoQuantity, int eachPrice, int giveaway) {
        this.productName = productName;
        this.quantity = quantity;
        this.nonPromoQuantity = nonPromoQuantity;
        this.eachPrice = eachPrice;
        this.giveaway = giveaway;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNonPromoQuantity() {
        return nonPromoQuantity;
    }

    public int getEachPrice() {
        return eachPrice;
    }

    public int getGiveaway() {
        return giveaway;
    }
}

