package store.ENUM;

public enum Product {
    COKE("콜라", 1000, 7, "탄산2+1"),
    COKE_EXTRA("콜라", 1000, 10, null),
    SODA("사이다", 1000, 8, "탄산2+1"),
    SODA_EXTRA("사이다", 1000, 7, null),
    ORANGE_JUICE("오렌지주스", 1800, 9, "MD추천상품"),
    ORANGE_JUICE_OUT("오렌지주스", 1800, 0, null),
    SPARKLING_WATER("탄산수", 1200, 5, "탄산2+1"),
    SPARKLING_WATER_OUT("탄산수", 1200, 0, null),
    WATER("물", 500, 10, null),
    VITAMIN_WATER("비타민워터", 1500, 6, null),
    POTATO_CHIPS("감자칩", 1500, 5, "반짝할인"),
    POTATO_CHIPS_EXTRA("감자칩", 1500, 5, null),
    CHOCOLATE_BAR("초코바", 1200, 5, "MD추천상품"),
    CHOCOLATE_BAR_EXTRA("초코바", 1200, 5, null),
    ENERGY_BAR_OUT("에너지바", 2000, 0, null),
    LUNCH_BOX("정식도시락", 6400, 8, null),
    CUP_NOODLES("컵라면", 1700, 1, "MD추천상품"),
    CUP_NOODLES_EXTRA("컵라면", 1700, 10, null);

    private final String name;
    private final int price;
    private int stock;
    private String promotion;

    Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int amount) {
        this.stock += amount;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void updatePromotion(String promotion) {
        this.promotion=promotion;
    }

    @Override
    public String toString() {
        String stockInfo="재고 없음";
        String promotion=getPromotion();
        if (promotion == null) {
            promotion="";
        }
        if(stock>0){
            stockInfo=stock+"개";
        }
        return "- " + name + " " + price + "원 " + stockInfo + (promotion);
    }
}


