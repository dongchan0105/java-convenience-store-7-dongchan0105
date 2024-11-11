package store.view;

import static store.constant.Constants.ADD_PROMOTION_QUANTITY;
import static store.constant.Constants.CONVENIENCE_ENTER;
import static store.constant.Constants.HAS_MEMBERSHIP_DISCOUNT;
import static store.constant.Constants.PURCHASE_QUANTITY;
import static store.constant.Constants.SHORTAGE_MESSAGE;

import camp.nextstep.edu.missionutils.Console;
import store.repository.ProductRepository;
import store.domain.Product;

public class InputView {
    private final ProductRepository productRepository;

    public InputView(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void openConvenienceStore() {
        System.out.println(CONVENIENCE_ENTER);
        productRepository.getAllProducts().forEach(product -> {
            StockInfo stockInfo = getStockInfo(product);

            String formattedPrice = String.format("%,d", product.getPrice());
            System.out.printf("- %s %s원 %s %s\n", product.getName(), formattedPrice, stockInfo.stockInfo(),
                    stockInfo.promotionInfo());
        });
    }

    private static StockInfo getStockInfo(Product product) {
        String promotionInfo = getPromotionInfo(product);
        String quantityInfo = getQuantityInfo(product);
        return new StockInfo(promotionInfo, quantityInfo);
    }

    private static String getPromotionInfo(Product product) {
        if (product.getPromotion() != null) {
            return product.getPromotion();
        }
        return "";
    }

    private static String getQuantityInfo(Product product) {
        if (product.getQuantity() > 0) {
            return product.getQuantity() + "개";
        }
        return "재고 없음";
    }

    private record StockInfo(String promotionInfo, String stockInfo) {
    }

    public static String getPurchaseList() {
        System.out.println(PURCHASE_QUANTITY);
        return Console.readLine();
    }

    public static String isHaveMembership() {
        System.out.println(HAS_MEMBERSHIP_DISCOUNT);
        return Console.readLine();
    }

    public static String showAdditionalQuantityMessage(String productName, int additionalQuantity) {
        System.out.printf(ADD_PROMOTION_QUANTITY, productName, additionalQuantity);
        return Console.readLine();
    }

    public static String getUserConfirmation() {
        return Console.readLine();
    }

    public static void showShortageMessage(String productName, int shortage) {
        System.out.printf(SHORTAGE_MESSAGE, productName, shortage);
    }


}
