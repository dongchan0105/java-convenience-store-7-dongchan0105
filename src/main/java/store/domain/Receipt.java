package store.domain;

public class Receipt {
    private final String productName;
    private final int quantity;
    private final int eachPrice;
    private final int giveaway;

    public Receipt(String productName, int quantity, int eachPrice, int giveaway) {
        this.productName = productName;
        this.quantity = quantity;
        this.eachPrice = eachPrice;
        this.giveaway = giveaway;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getEachPrice() {
        return eachPrice;
    }

    public int getGiveaway() {
        return giveaway;
    }
}

