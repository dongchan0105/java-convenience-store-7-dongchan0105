package store.view;

import static store.Constant.Constants.ADD_PROMOTION_QUANTITY;
import static store.Constant.Constants.CONVENIENCE_ENTER;
import static store.Constant.Constants.HAS_MEMBERSHIP_DISCOUNT;
import static store.Constant.Constants.PURCHASE_QUANTITY;
import static store.Constant.Constants.SHORTAGE_MESSAGE;

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
        String promotionInfo = "";
        if (product.getPromotion() != null) {
            promotionInfo = product.getPromotion();
        }
        String quantitiyInfo = "";
        if (product.getQuantity() > 0) {
            quantitiyInfo = product.getQuantity() + "개";
        } else {
            quantitiyInfo = "재고 없음";
        }
        return new StockInfo(promotionInfo, quantitiyInfo);
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
